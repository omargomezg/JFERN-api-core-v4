package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.getnet.GetnetRequest;

import java.util.Optional;

public interface PayService {

    Optional<PaymentResponse> getPayment(GetnetRequest request);

    PaymentResponse getPaymentStatus(String saleOrderId);

    void scheduledTaskForPendings();

    void updatePendingPayments();
}
