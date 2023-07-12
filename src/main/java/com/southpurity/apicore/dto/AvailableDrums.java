package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableDrums {

    @JsonView(View.Anonymous.class)
    private Integer available;

    @JsonView(View.Anonymous.class)
    private Integer price;

    @JsonView(View.Anonymous.class)
    private Integer priceWithDrum;
}
