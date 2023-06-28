package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableDrums {

    @JsonView(View.Customer.class)
    private Integer available;

    @JsonView(View.Customer.class)
    private Integer price;

    @JsonView(View.Customer.class)
    private Integer priceWithDrum;
}
