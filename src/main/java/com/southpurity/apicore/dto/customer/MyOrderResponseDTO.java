package com.southpurity.apicore.dto.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyOrderResponseDTO {
    private String id;
    private String correlative;
}
