package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.ProductsInPaymentResponse;
import com.southpurity.apicore.dto.payment.ClientDto;
import com.southpurity.apicore.dto.payment.ItemsDto;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.dto.payment.ProductDto;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.PayService;
import com.southpurity.apicore.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CashPaymentServiceImpl implements PayService {

    private final ProductRepository productRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Transactional
    @Override
    public PaymentResponse getPayment(PaymentRequest request) {
        var client = getClient(request.getClient());
        var products = markAsTakenAndGetProducts(request);
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .paymentType(PaymentTypeEnum.CASH)
                .build();
        var order = SaleOrderDocument.builder()
                .products(products)
                .status(SaleOrderStatusEnum.APPROVED)
                .client(client)
                .paymentDetail(paymentDetail);
        productRepository.deleteAll(products);
        saleOrderRepository.save(order.build());
        return PaymentResponse.builder()
                .message("Pago en efectivo")
                .products(products.stream().map(p -> ProductsInPaymentResponse.builder()
                        .key(p.getPadlockKey())
                        .value(p.getPadlockKey())
                        .build()).toList())
                .paymentStatus(SaleOrderStatusEnum.APPROVED.name())
                .build();
    }

    private List<ProductDocument> markAsTakenAndGetProducts(PaymentRequest request) {
        var productsDto = request.getProducts();
        var place = request.getPlace();
        if (productsDto.isEmpty()) {
            var quantity = request.getItems().stream().mapToInt(ItemsDto::getQuantity).sum();
            return productRepository.markAsTaken(quantity, place.getId());
        }
        var ids = productsDto.stream().map(ProductDto::getId).toList();
        return productRepository.markAsTaken(ids, place.getId());
    }

    @Override
    public PaymentResponse getPaymentStatus(String saleOrderId) {
        return null;
    }

    @Override
    public void scheduledTaskForPendings() {

    }

    @Override
    public void updatePendingPayments() {

    }

    @Override
    public PaymentTypeEnum getPaymentType() {
        return PaymentTypeEnum.CASH;
    }

    private UserDocument getClient(ClientDto clientDto) {
        if (clientDto.getId() == null) {
            return userRepository.findById(profileService.get().getId()).orElseThrow();
        }
        return userRepository.findById(clientDto.getId()).orElseThrow();
    }
}
