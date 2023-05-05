package com.southpurity.apicore.dto.customer;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

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
}
