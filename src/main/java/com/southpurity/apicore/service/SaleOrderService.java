package com.southpurity.apicore.service;

import com.placetopay.java_placetopay.Entities.Models.RedirectInformation;
import com.southpurity.apicore.dto.SaleOrderFilter;
import com.southpurity.apicore.dto.SaleOrderRequest;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SaleOrderService {

    // TODO 1: RedirectInformation se debe por un objeto generico en la interfz
    Optional<SaleOrderDocument> updatePaymentStatus(RedirectInformation payment, String saleOrderId);

    List<SaleOrderDocument> getAllOrdersByUser(String userId);

    Page<SaleOrderDocument> getAll(SaleOrderRequest request);

    void asyncTaskForCheckIncompleteTransactions(String saleOrderId);

}
