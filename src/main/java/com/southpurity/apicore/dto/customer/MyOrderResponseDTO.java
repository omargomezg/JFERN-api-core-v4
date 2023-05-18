package com.southpurity.apicore.dto.customer;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MyOrderResponseDTO {

    @JsonView(View.Customer.class)
    private String id;

    @JsonView(View.Customer.class)
    private String address;

    @JsonView(View.Customer.class)
    private Date date;
}
