package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

@Data
public class ProductDTO {

    @JsonView(View.Administrator.class)
    private String id;

    @JsonView(View.Customer.class)
    private String lockNumber;

    @JsonView(View.Customer.class)
    private String padlockKey;

    @JsonView(View.Customer.class)
    private String place;

}
