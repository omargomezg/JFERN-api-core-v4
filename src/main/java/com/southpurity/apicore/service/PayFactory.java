package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PayFactory {

    private final Map<PaymentTypeEnum, PayService> payServices = new HashMap<>();

    @Autowired
    public PayFactory(Set<PayService> payServiceSet) {
        populatePayServices(payServiceSet);
    }

    private void populatePayServices(Set<PayService> payServiceSet) {
        payServiceSet.forEach(payService -> payServices.put(payService.getPaymentType(), payService));
    }

    public PayService getStrategy(PaymentTypeEnum paymentTypeEnum) {
        return payServices.get(paymentTypeEnum);
    }
}
