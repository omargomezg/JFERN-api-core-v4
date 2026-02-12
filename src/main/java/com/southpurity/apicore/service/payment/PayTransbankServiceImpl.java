package com.southpurity.apicore.service.payment;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.ProductsInPaymentResponse;
import com.southpurity.apicore.dto.payment.ClientDto;
import com.southpurity.apicore.dto.payment.ItemsDto;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.persistence.model.BaseDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.History;
import com.southpurity.apicore.persistence.model.saleorder.ItemDocument;
import com.southpurity.apicore.persistence.model.saleorder.Key;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class PayTransbankServiceImpl implements PayService {

    private final ConfigurationRepository configurationRepository;
    private final PlaceRepository placeRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProfileService profileService;

    @Override
    public PaymentResponse getPayment(PaymentRequest request) {
        List<ProductDocument> products = new ArrayList<>();
        PlaceDocument place = placeRepository.findById(request.getPlace().getId()).orElseThrow();
        var client = getClient(request.getClient());

        request.getItems().forEach(item -> products.addAll(productRepository.markAsTaken(
                item.getQuantity(),
                request.getPlace().getId(),
                item.getDescription())));
        var order = createOrder(client, products, request.getItems());

        var configuration = configurationRepository.findBySiteName("southpurity").orElseThrow();
        var returnUrl = configuration.getReturnUrl() + "/" + order.getId();
        String buyOrder = order.getId();
        String sessionId = client.getId();
        double amount = order.getItems().stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum();

        var tx = new WebpayPlus.Transaction(new WebpayOptions(
                IntegrationCommerceCodes.WEBPAY_PLUS,
                IntegrationApiKeys.WEBPAY,
                IntegrationType.TEST));
        try {
            var resp = tx.create(buyOrder, sessionId, amount, returnUrl);

            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .token(resp.getToken())
                    .paymentType(PaymentTypeEnum.TRANSBANK)
                    .status(SaleOrderStatusEnum.PENDING.name())
                    .build();
            order.setPaymentDetail(paymentDetail);
            order.getHistory().add(History.builder()
                    .message("Pago iniciado en Transbank. Token: " + resp.getToken())
                    .build());
            saleOrderRepository.save(order);

            return PaymentResponse.builder()
                    .url(String.format("%s?token_ws=%s", resp.getUrl(), resp.getToken()))
                    .message(resp.getToken()) // For frontend use if needed
                    .token(resp.getToken())
                    .saleOrderId(order.getId())
                    .build();
        } catch (Exception e) {
            log.error("Error creating Webpay transaction", e);
            productRepository.markAsAvailable(products.stream().map(BaseDocument::getId).toList(), place.getId());
            order.setStatus(SaleOrderStatusEnum.REJECTED); // or UNKNOWN
            order.getHistory().add(History.builder()
                    .message("Error iniciando pago Transbank: " + e.getMessage())
                    .build());
            saleOrderRepository.save(order);
        }
        return PaymentResponse.builder().message("Error initiating payment").build();
    }

    @Override
    public PaymentResponse getPaymentStatus(String saleOrderId) {
        PaymentResponse response = new PaymentResponse();
        saleOrderRepository.findById(saleOrderId).ifPresentOrElse(saleOrder -> {
            var tx = new WebpayPlus.Transaction(new WebpayOptions(
                    IntegrationCommerceCodes.WEBPAY_PLUS,
                    IntegrationApiKeys.WEBPAY,
                    IntegrationType.TEST));
            try {
                String token = saleOrder.getPaymentDetail().getToken();
                if (token == null) {
                    response.setPaymentStatus("NO_TOKEN");
                    return;
                }

                // Try to commit (if returning from Webpay) or status (if checking later)
                // Note: The Webpay SDK 'commit' should be called once.
                // If the order is already approved/rejected, we shouldn't commit again.
                if (SaleOrderStatusEnum.PENDING.equals(saleOrder.getStatus()) || saleOrder.getStatus() == null) {
                    var result = tx.commit(token);

                    if (result.getResponseCode() == 0) { // 0 = Authorized
                        saleOrder.setStatus(SaleOrderStatusEnum.APPROVED);
                        // Logic to release products
                        saleOrder.getProducts().forEach(product -> saleOrder.getKeys().add(productToKey(product)));
                        productRepository.deleteAll(saleOrder.getProducts());
                        saleOrder.getProducts().clear();

                        saleOrder.getPaymentDetail().setStatus("AUTHORIZED");
                        saleOrder.getPaymentDetail().setAuthorizationCode(result.getAuthorizationCode());
                        saleOrder.getHistory().add(History.builder().message("Pago Aprobado Transbank").build());
                    } else {
                        saleOrder.setStatus(SaleOrderStatusEnum.REJECTED);
                        releaseProducts(saleOrder);
                        saleOrder.getPaymentDetail().setStatus("REJECTED");
                        saleOrder.getHistory().add(History.builder()
                                .message("Pago Rechazado Transbank. Code: " + result.getResponseCode()).build());
                    }
                    saleOrderRepository.save(saleOrder);
                    response.setPaymentStatus(saleOrder.getStatus().name());
                } else {
                    // Already processed, just return status
                    response.setPaymentStatus(saleOrder.getStatus().name());
                }

                response.setProducts(
                        saleOrder.getKeys().stream()
                                .map(this::productToResponse)
                                .collect(Collectors.toSet()));

            } catch (Exception e) {
                log.error("Error checking Webpay status", e);
                // If commit fails (e.g. token expired, or already committed), we might want to
                // check status
                try {
                    var statusResult = tx.status(saleOrder.getPaymentDetail().getToken());
                    log.info("Webpay status check result: {}", statusResult);
                    // Update logic based on statusResult if needed
                    response.setPaymentStatus("ERROR_CHECKING_STATUS");
                } catch (Exception ex) {
                    response.setPaymentStatus("ERROR");
                }
            }
        }, () -> response.setPaymentStatus("NOT_EXISTS"));
        return response;
    }

    @Scheduled(fixedDelay = 86400000)
    @Override
    public void scheduledTaskForPendings() {
        Date sevenDaysAgo = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
        saleOrderRepository.findAllByStatusIn(SaleOrderStatusEnum.isPending()).parallelStream()
                .forEach(saleOrder -> {
                    if (saleOrder.getCreatedDate() != null && saleOrder.getCreatedDate().before(sevenDaysAgo)) {
                        saleOrder.setStatus(SaleOrderStatusEnum.REJECTED);
                        releaseProducts(saleOrder);
                        saleOrder.getHistory().add(History.builder()
                                .message("Expirado: El pago no fue confirmado en más de 7 días (Límite Transbank)")
                                .build());
                        saleOrderRepository.save(saleOrder);
                        return;
                    }
                    if (saleOrder.getPaymentDetail() != null && saleOrder.getPaymentDetail().getToken() != null) {
                        var tx = new WebpayPlus.Transaction(new WebpayOptions(
                                IntegrationCommerceCodes.WEBPAY_PLUS,
                                IntegrationApiKeys.WEBPAY,
                                IntegrationType.TEST));
                        try {
                            var status = tx.status(saleOrder.getPaymentDetail().getToken());
                            if ("AUTHORIZED".equals(status.getStatus())) {
                                saleOrder.setStatus(SaleOrderStatusEnum.APPROVED);
                                saleOrder.getProducts()
                                        .forEach(product -> saleOrder.getKeys().add(productToKey(product)));
                                productRepository.deleteAll(saleOrder.getProducts());
                                saleOrder.getProducts().clear();

                                saleOrder.getPaymentDetail().setStatus("AUTHORIZED");
                                saleOrder.getPaymentDetail().setAuthorizationCode(status.getAuthorizationCode());
                                saleOrder.getHistory()
                                        .add(History.builder().message("Pago Aprobado Transbank (Programado)").build());
                            } else if ("FAILED".equals(status.getStatus()) || "REJECTED".equals(status.getStatus())) {
                                saleOrder.setStatus(SaleOrderStatusEnum.REJECTED);
                                releaseProducts(saleOrder);
                                saleOrder.getPaymentDetail().setStatus(status.getStatus());
                                saleOrder.getHistory().add(History.builder()
                                        .message("Pago Rechazado/Fallido Transbank (Programado). Code: "
                                                + status.getResponseCode())
                                        .build());
                            }
                            saleOrderRepository.save(saleOrder);
                            log.info("Scheduled check for order {}: {}", saleOrder.getId(), status.getStatus());
                        } catch (Exception e) {
                            log.error("Error in scheduled task for order " + saleOrder.getId(), e);
                        }
                    }
                });
    }

    @Override
    public void updatePendingPayments() {
        scheduledTaskForPendings();
    }

    private UserDocument getClient(ClientDto clientDto) {
        if (clientDto.getId() == null) {
            return userRepository.findById(profileService.get().getId()).orElseThrow();
        }
        return userRepository.findById(clientDto.getId()).orElseThrow();
    }

    private SaleOrderDocument createOrder(UserDocument client, List<ProductDocument> products,
            List<ItemsDto> itemsDto) {
        List<ItemDocument> items = getItems(itemsDto);
        SaleOrderDocument saleOrder = SaleOrderDocument.builder()
                .client(client)
                .products(products)
                .items(items)
                .build();
        return saleOrderRepository.save(saleOrder);
    }

    private List<ItemDocument> getItems(List<ItemsDto> items) {
        return items.stream().map(itemsDto -> ItemDocument.builder()
                .price(itemsDto.getPrice())
                .name(itemsDto.getDescription())
                .quantity(Long.valueOf(itemsDto.getQuantity()))
                .money(CurrencyEnum.CLP)
                .build()).toList();
    }

    private Key productToKey(ProductDocument product) {
        return Key.builder()
                .key(product.getLockNumber())
                .value(product.getPadlockKey())
                .build();
    }

    private ProductsInPaymentResponse productToResponse(Key key) {
        return ProductsInPaymentResponse.builder()
                .key(key.getKey())
                .value(key.getValue())
                .build();
    }

    private void releaseProducts(SaleOrderDocument saleOrder) {
        saleOrder.getProducts().forEach(product -> {
            product.setStatus(OrderStatusEnum.AVAILABLE);
            productRepository.save(product);
        });
    }

    @Override
    public PaymentTypeEnum getPaymentType() {
        return PaymentTypeEnum.TRANSBANK;
    }
}
