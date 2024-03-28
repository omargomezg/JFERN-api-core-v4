package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {

    @JsonView(View.Administrator.class)
    private String id;

    @JsonView(View.Customer.class)
    private String lockNumber;

    @JsonView(View.Customer.class)
    private String padlockKey;

    /**
     * The place id where the product is located.
     */
    @JsonView(View.Customer.class)
    private String place;


    @NotNull
    @JsonView(View.Customer.class)
    private String productType;

}
