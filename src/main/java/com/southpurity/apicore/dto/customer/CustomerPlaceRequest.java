package com.southpurity.apicore.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerPlaceRequest {

    /**
     * Id de ta tabla place
     */
    @NotEmpty
    private String idPlace;

    /**
     * Corresponde al departamento, número de casa o cualquier
     * texto que identifique la dirección exacta
     */
    @NotEmpty
    private String address;

    @Builder.Default
    private Boolean isPrincipal = false;
}
