package com.southpurity.apicore.service.impl;

import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    @Override
    public Optional<SaleOrderDocument> updatePaymentStatus(RedirectInformation payment, String saleOrderId) {
        if (payment.getStatus().isApproved()) {
            return Optional.of(saveApprovedPayment(payment, saleOrderId));
        }
        return Optional.empty();
    }

    @Override
    public void addPayment(String saleOrderId, Integer requestId, String processUrl) {
        var saleOrder = saleOrderRepository.findById(saleOrderId).orElseThrow();
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setRequestId(requestId);
        paymentDetail.setProcessUrl(processUrl);
        paymentDetail.setStatus("PENDING");
        saleOrder.setPaymentDetail(paymentDetail);
        saleOrderRepository.save(saleOrder);
    }

    @Override
    public List<SaleOrderDocument> getAllOrdersByUser(String userId) {
        var client = userRepository.findById(userId).orElseThrow();
        return saleOrderRepository.findByClient(client);
    }

    @Override
    public Page<SaleOrderDocument> getAll(String userId, Pageable pageable) {
        var user = userRepository.findById(userId).orElseThrow();
        var page = saleOrderRepository.findAllByClient(user, pageable);
        page.getContent().forEach(this::sumTotal);
        return page;
    }

    @Async
    @Override
    public void asyncTaskForCheckIncompleteTransactions(SaleOrderDocument saleOrderDocument) {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        saleOrderDocument = saleOrderRepository.findById(saleOrderDocument.getId()).orElseThrow();
        // check status, if null return
        if (saleOrderDocument.getPaymentDetail() == null) {
            return;
        }
        if (saleOrderDocument.getPaymentDetail().getStatus().equals("PENDING")) {
            saleOrderDocument.getProducts().forEach(this::releaseProduct);
            saleOrderRepository.save(saleOrderDocument);
        }
        saleOrderDocument.setStatus(SaleOrderStatusEnum.TIMEOUT);
        saleOrderRepository.save(saleOrderDocument);
    }

    private void releaseProduct(ProductDocument productDocument) {
        productDocument.setStatus(OrderStatusEnum.AVAILABLE);
        productRepository.save(productDocument);
    }

    private void sumTotal(SaleOrderDocument saleOrder) {
        var total = saleOrder.getItems().stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum();
        saleOrder.setTotal(total);
    }


    private SaleOrderDocument saveApprovedPayment(RedirectInformation payment, String saleOrderId) {
        var saleOrder = saleOrderRepository.findById(saleOrderId).orElseThrow();
        saleOrder.getPaymentDetail().setStatus(payment.getStatus().getStatus());
        saleOrder.getPaymentDetail().setReason(payment.getStatus().getReason());
        saleOrder.getPaymentDetail().setMessage(payment.getStatus().getMessage());
        saleOrder.getPaymentDetail().setDate(payment.getStatus().getDate());
        return saleOrderRepository.save(saleOrder);
    }
}
