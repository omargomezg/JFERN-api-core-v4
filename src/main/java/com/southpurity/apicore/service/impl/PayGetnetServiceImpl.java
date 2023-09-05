package com.southpurity.apicore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.placetopay.java_placetopay.Entities.Models.RedirectRequest;
import com.placetopay.java_placetopay.Entities.Models.RedirectResponse;
import com.placetopay.java_placetopay.PlaceToPay;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.ProductsInPaymentResponse;
import com.southpurity.apicore.dto.getnet.Amount;
import com.southpurity.apicore.dto.getnet.GetnetRequest;
import com.southpurity.apicore.dto.getnet.Payment;
import com.southpurity.apicore.dto.getnet.Person;
import com.southpurity.apicore.dto.payment.ClientDto;
import com.southpurity.apicore.dto.payment.ItemsDto;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.exception.PaymentException;
import com.southpurity.apicore.persistence.model.BaseDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.ItemDocument;
import com.southpurity.apicore.persistence.model.saleorder.Key;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.PayService;
import com.southpurity.apicore.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class PayGetnetServiceImpl implements PayService {

    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ConfigurationRepository configurationRepository;
    private final PlaceRepository placeRepository;
    private final ProfileService profileService;

    @Value("${getnet.endpoint}")
    private String endpoint;
    @Value("${getnet.login}")
    private String login;
    @Value("${getnet.trankey}")
    private String trankey;

    @Transactional
    @Override
    public PaymentResponse getPayment(PaymentRequest request) {
        PlaceDocument place = placeRepository.findById(request.getPlace().getId()).orElseThrow();
        var client = getClient(request.getClient());
        var products = productRepository.markAsTaken(request.getItems().stream().mapToInt(ItemsDto::getQuantity).sum(),
                request.getPlace().getId());
        var order = createOrder(client, products, request.getItems());
        PlaceToPay placeToPay = new PlaceToPay(login, trankey, getUrl());
        var getnetRequest = getGetnetRequest(request, client, order);
        RedirectRequest redirectRequest = toRedirectRequest(getnetRequest);
        RedirectResponse response = placeToPay.request(redirectRequest);
        if (!response.isSuccessful()) {
            productRepository.markAsAvailable(products.stream().map(BaseDocument::getId).toList(), place.getId());
            order.setStatus(SaleOrderStatusEnum.UNKNOWN);
            saleOrderRepository.save(order);
            log.error(response.getStatus().getMessage());
            return PaymentResponse.builder().message(response.getStatus().getMessage()).build();
        }
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .requestId(response.requestId)
                .processUrl(response.processUrl)
                .status(SaleOrderStatusEnum.PENDING.name()).build();
        order.setPaymentDetail(paymentDetail);
        saleOrderRepository.save(order);
        return PaymentResponse.builder().url(response.getProcessUrl())
                .requestId(response.getRequestId())
                .processUrl(response.getProcessUrl())
                .paymentStatus(SaleOrderStatusEnum.PENDING.name())
                .build();
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

    private GetnetRequest getGetnetRequest(PaymentRequest request, UserDocument client, SaleOrderDocument order) {
        var configuration = configurationRepository.findBySiteName("southpurity").orElseThrow();
        var returnUrl = configuration.getReturnUrl() + "/" + order.getId();

        Amount amount = Amount.builder()
                .currency("CLP")
                .total(String.valueOf(order.getItems().stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum()))
                .build();
        Person buyer = new Person();
        buyer.setDocumentType("CLRUT");
        buyer.setDocument(client.getRut());
        buyer.setName(client.getFullName());
        buyer.setEmail(client.getEmail());
        Payment payment = new Payment();
        payment.setReference(order.getId());
        payment.setAmount(amount);
        payment.setBuyer(buyer);
        return GetnetRequest.builder()
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .returnUrl(returnUrl)
                .payment(payment)
                .build();
    }

    private RedirectRequest toRedirectRequest(GetnetRequest getnetRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return new RedirectRequest(objectMapper.writeValueAsString(getnetRequest));
        } catch (JsonProcessingException e) {
            log.error("Error parsing GetnetRequest to RedirectRequest", e);
            throw new PaymentException("Error parsing GetnetRequest to RedirectRequest");
        }
    }

    @Override
    public PaymentResponse getPaymentStatus(String saleOrderId) {
        var saleOrder = saleOrderRepository.findById(saleOrderId);
        if (saleOrder.isEmpty() || saleOrder.get().getPaymentDetail() == null) {
            return PaymentResponse.builder()
                    .paymentStatus("NOT_EXISTS")
                    .build();
        }
        PlaceToPay placeToPay = new PlaceToPay(login, trankey, getUrl());
        var resultQuery = placeToPay.query(saleOrder.get().getPaymentDetail().getRequestId().toString());
        log.info("Payment status: {}", resultQuery.toJsonObject());
        if (resultQuery.getStatus().isApproved()) {
            saleOrder.get().getProducts().forEach(product -> saleOrder.get().getKeys().add(productToKey(product)));
            productRepository.deleteAll(saleOrder.get().getProducts());
            saleOrder.get().getProducts().clear();
        }
        addPaymentStatusToSaleOrder(resultQuery, saleOrder.get());
        return PaymentResponse.builder()
                .products(
                        saleOrder.get().getKeys().stream()
                                .map(this::productToResponse)
                                .collect(Collectors.toSet()))
                .paymentStatus(resultQuery.getStatus().getStatus())
                .build();
    }

    /**
     * Every 24 hours, the system will check for pending payments and will update the status of the sale order
     */
    @Scheduled(fixedDelay = 86400000)
    @Override
    public void scheduledTaskForPendings() {
        var saleOrders = saleOrderRepository.findAllByStatus(SaleOrderStatusEnum.PENDING);
        saleOrders.forEach(saleOrder -> {
            log.info("Updating payment status for sale order {}", saleOrder.getId());
            PlaceToPay placeToPay = new PlaceToPay(login, trankey, getUrl());
            if (saleOrder.getPaymentDetail() != null) {
                var resultQuery = placeToPay.query(saleOrder.getPaymentDetail().getRequestId().toString());
                log.info("Payment status: {}", resultQuery.toJsonObject());
                addPaymentStatusToSaleOrder(resultQuery, saleOrder);
            }
        });
    }

    @Override
    public void updatePendingPayments() {
        this.scheduledTaskForPendings();
    }

    @Override
    public PaymentTypeEnum getPaymentType() {
        return PaymentTypeEnum.GETNET;
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

    private void addPaymentStatusToSaleOrder(RedirectInformation redirectInformation, SaleOrderDocument saleOrder) {
        SaleOrderStatusEnum status = SaleOrderStatusEnum.valueOf(redirectInformation.getStatus().getStatus());
        saleOrder.setStatus(status);
        if (status.equals(SaleOrderStatusEnum.REJECTED)) {
            saleOrder.getProducts().forEach(product -> {
                product.setStatus(OrderStatusEnum.AVAILABLE);
                productRepository.save(product);
            });
        }
        saleOrderRepository.save(saleOrder);
    }

    private URL getUrl() {
        try {
            return new URL(endpoint);
        } catch (MalformedURLException e) {
            log.error("Error creating URL", e);
            throw new PaymentException("Error creating URL");
        }
    }
}
