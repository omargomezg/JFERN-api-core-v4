package com.southpurity.apicore.service.impl;

import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
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

    private void sumTotal(SaleOrderDocument saleOrder) {
        var total = saleOrder.getItems().stream().mapToLong(item -> (long) item.getPrice() * item.getQuantity()).sum();
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
