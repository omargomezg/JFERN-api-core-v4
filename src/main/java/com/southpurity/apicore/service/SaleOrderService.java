package com.southpurity.apicore.service;

import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;

import java.util.List;
import java.util.Optional;

public interface SaleOrderService {

    // TODO 1: RedirectInformation se debe por un objeto generico en la interfz
    Optional<SaleOrderDocument> updatePaymentStatus(RedirectInformation payment, String saleOrderId);

    void addPayment(String saleOrderId, Integer requestId, String processUrl);

    List<SaleOrderDocument> getAllOrdersByUser(String userId);

}
