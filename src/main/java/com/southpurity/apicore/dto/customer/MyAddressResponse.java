package com.southpurity.apicore.dto.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyAddressResponse {
    private String id;
    private String fullAddress;
    private Boolean isPrimary;
}
