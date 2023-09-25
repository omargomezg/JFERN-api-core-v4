package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.service.EmailService;
import com.southpurity.apicore.service.PayFactory;
import com.southpurity.apicore.service.SaleOrderService;
import com.southpurity.apicore.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final SaleOrderService saleOrderService;
    private final EmailService emailService;
    private final PayFactory payFactory;

    @GetMapping("/client/{id}")
    public ResponseEntity<List<SaleOrderDocument>> getAllOrdersByUser(@PathVariable String id) {
        return ResponseEntity.ok(saleOrderService.getAllOrdersByUser(id));
    }

    @PatchMapping
    public ResponseEntity<Void> updatePendingPayments() {
        var service = payFactory.getStrategy(PaymentTypeEnum.GETNET);
        service.updatePendingPayments();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> getPayment(@RequestBody PaymentRequest request,
                                                      HttpServletRequest httpServletRequest) {
        request.setIpAddress(Utils.getIpAddress(httpServletRequest));
        request.setUserAgent(Utils.getUserAgent(httpServletRequest));
        var service = payFactory.getStrategy(PaymentTypeEnum.valueOf(request.getPaymentType()));
        PaymentResponse response = service.getPayment(request);
        saleOrderService.asyncTaskForCheckIncompleteTransactions(response.getSaleOrderId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{saleOrderId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable String saleOrderId) {
        var service = payFactory.getStrategy(PaymentTypeEnum.GETNET);
        var result = service.getPaymentStatus(saleOrderId);
        if (result.getPaymentStatus().equals("APPROVED")) {
            emailService.sendPurchaseEmail(saleOrderId);
        }
        return ResponseEntity.ok(result);
    }

}
