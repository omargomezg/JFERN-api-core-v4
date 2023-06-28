package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
public class PaymentResponse {

    @JsonView(View.Anonymous.class)
    private String url;

    @JsonView(View.Anonymous.class)
    private String message;

    @JsonView(View.Customer.class)
    private Collection<ProductsInPaymentResponse> products;

    /**
     * Payment order id
     */
    private Integer requestId;

    /**
     * Payment order url
     */
    private String processUrl;

    @JsonView(View.Customer.class)
    private String paymentStatus;
}
