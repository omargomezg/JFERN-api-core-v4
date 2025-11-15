package com.southpurity.apicore.dto.getnet;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class Payment {

    @NotEmpty
    private String reference;

    private Person buyer;

    @NotNull
    private Amount amount;


}
