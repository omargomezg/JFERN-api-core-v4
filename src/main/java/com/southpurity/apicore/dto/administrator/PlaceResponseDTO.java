package com.southpurity.apicore.dto.administrator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceResponseDTO {
    private String id;
    private String address;

    /**
     * Padlocks available in place
     */
    private int padlocks;
}
