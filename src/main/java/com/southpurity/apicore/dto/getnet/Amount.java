package com.southpurity.apicore.dto.getnet;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class Amount {

    @NotEmpty
    @Builder.Default
    private String currency = "CLP";

    @NotEmpty
    private String total;
}
