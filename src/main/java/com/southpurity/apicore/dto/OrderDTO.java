package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

@Data
public class OrderDTO {

    @JsonView(View.Administrator.class)
    private String id;

    @JsonView(View.Customer.class)
    private Integer lockNumber;

    @JsonView(View.Customer.class)
    private Integer padlockKey;

    @JsonView(View.Customer.class)
    private String place;

}
