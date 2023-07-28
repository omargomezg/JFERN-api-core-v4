package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.CartRequest;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.getnet.Amount;
import com.southpurity.apicore.dto.getnet.GetnetRequest;
import com.southpurity.apicore.dto.getnet.Payment;
import com.southpurity.apicore.dto.getnet.Person;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.service.ConfigurationService;
import com.southpurity.apicore.service.PayService;
import com.southpurity.apicore.service.ProductService;
import com.southpurity.apicore.service.ProfileService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayService payService;
    private final SaleOrderService saleOrderService;
    private final ProfileService profileService;
    private final ProductService productService;
    private final ConfigurationService configurationService;

    @GetMapping("/client/{id}")
    public ResponseEntity<List<SaleOrderDocument>> getAllOrdersByUser(@PathVariable String id) {
        return ResponseEntity.ok(saleOrderService.getAllOrdersByUser(id));
    }

    @PatchMapping
    public ResponseEntity<Void> updatePendingPayments() {
        payService.updatePendingPayments();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> getPayment(@RequestParam String address,
                                                      @RequestBody List<CartRequest> cartRequest,
                                                      HttpServletRequest httpServletRequest) {
        var authenticatedUser = profileService.get();
        var saleOrder = productService.takeOrder(address, cartRequest, authenticatedUser);
        Amount amount = Amount.builder()
                .currency("CLP")
                .total(String.valueOf(saleOrder.getItems().stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum()))
                .build();
        Person buyer = new Person();
        buyer.setDocumentType("CLRUT");
        buyer.setDocument(authenticatedUser.getRut());
        buyer.setName(authenticatedUser.getFullName());
        buyer.setEmail(authenticatedUser.getEmail());
        Payment payment = new Payment();
        payment.setReference(saleOrder.getId());
        payment.setAmount(amount);
        payment.setBuyer(buyer);
        GetnetRequest getnetRequest = GetnetRequest.builder()
                .ipAddress(Utils.getIpAddress(httpServletRequest))
                .userAgent(Utils.getUserAgent(httpServletRequest))
                .returnUrl(
                        configurationService.get().getReturnUrl() + "/" + saleOrder.getId()
                )
                .payment(payment)
                .build();

        getnetRequest.setPayment(payment);
        var resultPayment = payService.getPayment(getnetRequest);
        if (resultPayment.isEmpty()) {
            productService.cancelOrder(authenticatedUser.getId());
            return ResponseEntity.badRequest().body(PaymentResponse.builder().message("Error al procesar el pago").build());
        }
        saleOrderService.addPayment(saleOrder.getId(), resultPayment.get().getRequestId(), resultPayment.get().getProcessUrl());
        saleOrderService.asyncTaskForCheckIncompleteTransactions(saleOrder);
        return ResponseEntity.ok(resultPayment.get());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable String id) {
        return ResponseEntity.ok(payService.getPaymentStatus(id));
    }

}
