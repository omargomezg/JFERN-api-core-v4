package com.southpurity.apicore.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressDocument {
    private String placeId;
    private String address;
}
