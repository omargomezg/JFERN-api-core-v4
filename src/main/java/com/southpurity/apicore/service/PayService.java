package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;

public interface PayService {

    PaymentResponse getPayment(PaymentRequest request);

    PaymentResponse getPaymentStatus(String saleOrderId);

    void scheduledTaskForPendings();

    void updatePendingPayments();

    PaymentTypeEnum getPaymentType();
}
