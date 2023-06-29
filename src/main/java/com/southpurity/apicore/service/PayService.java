package com.southpurity.apicore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.getnet.GetnetRequest;

public interface PayService {

    PaymentResponse getPayment(GetnetRequest request) throws JsonProcessingException;

    PaymentResponse getPaymentStatus(String saleOrderId);

    void scheduledTaskForPendings();

    void updatePendingPayments();
}
