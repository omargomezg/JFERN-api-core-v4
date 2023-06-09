package com.southpurity.apicore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.placetopay.java_placetopay.Entities.Models.RedirectRequest;
import com.placetopay.java_placetopay.Entities.Models.RedirectResponse;
import com.placetopay.java_placetopay.PlaceToPay;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.ProductsInPaymentResponse;
import com.southpurity.apicore.dto.getnet.GetnetRequest;
import com.southpurity.apicore.exception.PaymentException;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.Key;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PayGetnetServiceImpl implements PayService {

    private final SaleOrderRepository saleOrderRepository;
    private final ProductRepository productRepository;
    @Value("${getnet.endpoint}")
    private String endpoint;
    @Value("${getnet.login}")
    private String login;
    @Value("${getnet.trankey}")
    private String trankey;

    @Override
    public PaymentResponse getPayment(GetnetRequest getnetRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PlaceToPay placeToPay = new PlaceToPay(login, trankey, getUrl());

        RedirectRequest request = new RedirectRequest(objectMapper.writeValueAsString(getnetRequest));
        RedirectResponse response = placeToPay.request(request);
        if (response.isSuccessful()) {
            // STORE THE response.getRequestId() and response.getProcessUrl() on your DB associated with the payment order
            // Redirect the client to the processUrl or display it on the JS extension
            return PaymentResponse.builder().url(response.getProcessUrl())
                    .requestId(response.getRequestId())
                    .processUrl(response.getProcessUrl())
                    .build();
        } else {
            // There was some error so check the message and log it
            log.error(request.getIpAddress());
            log.error(request.getUserAgent());
            throw new PaymentException(response.getStatus().getMessage());
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
            PlaceToPay placeToPay = new PlaceToPay(login, trankey, getUrl());
            if (saleOrder.getPaymentDetail() != null) {
                var resultQuery = placeToPay.query(saleOrder.getPaymentDetail().getRequestId().toString());
                addPaymentStatusToSaleOrder(resultQuery, saleOrder);
            }
        });
    }

    @Override
    public void updatePendingPayments() {
        this.scheduledTaskForPendings();
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
