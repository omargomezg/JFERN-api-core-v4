package com.southpurity.apicore.dto.getnet;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Amount {

    @NotEmpty
    @Builder.Default
    private String currency = "CLP";

    @NotEmpty
    private String total;
}
