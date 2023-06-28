package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductsInPaymentResponse {

    @JsonView(View.Anonymous.class)
    private String key;

    @JsonView(View.Anonymous.class)
    private String value;
}
