package com.southpurity.apicore.dto.getnet;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Amount {

    @NotEmpty
    private String currency = "CLP";

    @NotEmpty
    private String total;
}
