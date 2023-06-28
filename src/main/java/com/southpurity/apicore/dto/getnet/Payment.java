package com.southpurity.apicore.dto.getnet;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Payment {

    @NotEmpty
    private String reference;

    private Person buyer;

    @NotNull
    private Amount amount;


}
