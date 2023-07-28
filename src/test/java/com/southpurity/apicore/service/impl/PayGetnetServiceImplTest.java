package com.southpurity.apicore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.getnet.Amount;
import com.southpurity.apicore.dto.getnet.GetnetRequest;
import com.southpurity.apicore.dto.getnet.Payment;
import com.southpurity.apicore.dto.getnet.Person;
import com.southpurity.apicore.exception.PaymentException;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PayGetnetServiceImplTest {

    private AutoCloseable closeable;

    @InjectMocks
    private PayGetnetServiceImpl payGetnetService;

    @Mock
    private SaleOrderRepository saleOrderRepository;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(payGetnetService, "endpoint", "https://checkout.test.getnet.cl");
        ReflectionTestUtils.setField(payGetnetService, "login", "7ffbb7bf1f7361b1200b2e8d74e1d76f");
        ReflectionTestUtils.setField(payGetnetService, "trankey", "SnZP3D63n3I9dH9O");
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void getPayment_success() throws JsonProcessingException {
        Payment payment = new Payment();
        payment.setReference("88236498268");
        payment.setAmount(Amount.builder().currency("CLP").total("1000").build());
        payment.setBuyer(new Person());
        GetnetRequest getnetRequest = GetnetRequest.builder()
                .ipAddress("192.168.0.1")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .returnUrl("https://www.southpurity.com/check-status")
                .payment(payment)
                .build();

        getnetRequest.setPayment(payment);

        var result = payGetnetService.getPayment(getnetRequest);
        assertThat(result).isPresent();
        assertNotNull(result.get().getRequestId());
        assertNotNull(result.get().getProcessUrl());
    }

    @Disabled
    @Test
    void getPaymentStatus_isOk() {
        // when(placeToPay.query("123")).thenReturn(new RedirectInformation(Status.ST_OK));
        var result = payGetnetService.getPaymentStatus("6494d79b243b5d3892ebe44b");
        assertNotNull(result);
    }

    @Disabled
    @Test
    void getPaymentStatus_with_MalformedURLException() {
        PaymentException exception = assertThrows(PaymentException.class, () -> payGetnetService.getPaymentStatus("123"));
        assertEquals("Error creating URL", exception.getMessage());
    }

    @Disabled
    @Test
    void getPaymentStatus_isNOk() {
        PaymentDetail paymentDetail = new PaymentDetail();
        SaleOrderDocument saleOrder = SaleOrderDocument.builder()
                .paymentDetail(paymentDetail)
                .build();
        when(saleOrderRepository.findById(any())).thenReturn(Optional.of(saleOrder));
        PaymentResponse result = payGetnetService.getPaymentStatus("123");
        assertNotNull(result);
    }
}
