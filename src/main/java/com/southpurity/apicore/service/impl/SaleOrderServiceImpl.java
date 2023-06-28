package com.southpurity.apicore.service.impl;

import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private final SaleOrderRepository saleOrderRepository;
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


    private SaleOrderDocument saveApprovedPayment(RedirectInformation payment, String saleOrderId) {
        var saleOrder = saleOrderRepository.findById(saleOrderId).orElseThrow();
        saleOrder.getPaymentDetail().setStatus(payment.getStatus().getStatus());
        saleOrder.getPaymentDetail().setReason(payment.getStatus().getReason());
        saleOrder.getPaymentDetail().setMessage(payment.getStatus().getMessage());
        saleOrder.getPaymentDetail().setDate(payment.getStatus().getDate());
        return saleOrderRepository.save(saleOrder);
    }
}
