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
import com.southpurity.apicore.utils.HttpServletRequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
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


    @PostMapping
    public ResponseEntity<PaymentResponse> getPayment(@RequestParam String address,
                                                      @RequestBody List<CartRequest> cartRequest,
                                                      HttpServletRequest httpServletRequest) {
        var saleOrder = productService.takeOrder(address, cartRequest, profileService.get());
        Amount amount = new Amount();
        amount.setCurrency("CLP");
        amount.setTotal(saleOrder.getItems().stream().map(item -> item.getPrice() * item.getQuantity())
                .reduce(0, Integer::sum).toString());
        Person buyer = new Person();
        buyer.setDocumentType("CLRUT");
        buyer.setDocument(profileService.get().getRut());
        buyer.setName(profileService.get().getFullName());
        buyer.setEmail(profileService.get().getEmail());
        Payment payment = new Payment();
        payment.setReference(saleOrder.getId());
        payment.setAmount(amount);
        payment.setBuyer(buyer);
        GetnetRequest getnetRequest = GetnetRequest.builder()
                .ipAddress(HttpServletRequestUtil.getIpAddress(httpServletRequest))
                .userAgent(HttpServletRequestUtil.getUserAgent(httpServletRequest))
                .returnUrl(
                        configurationService.get().getReturnUrl() + "/" + saleOrder.getId()
                )
                .payment(payment)
                .build();

        getnetRequest.setPayment(payment);
        try {
            var resultPayment = payService.getPayment(getnetRequest);
            saleOrderService.addPayment(saleOrder.getId(), resultPayment.getRequestId(), resultPayment.getProcessUrl());
            return ResponseEntity.ok(resultPayment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(PaymentResponse.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable String id) {
        return ResponseEntity.ok(payService.getPaymentStatus(id));
    }

}
