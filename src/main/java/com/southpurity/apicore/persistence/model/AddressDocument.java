package com.southpurity.apicore.persistence.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Builder
@Getter
public class AddressDocument {
    @DBRef
    private PlaceDocument place;

    private String address;

    @Builder.Default
    private Boolean isPrincipal = false;

    public String fullAddress() {
        return new StringBuilder()
                .append(place.getAddress())
                .append(", ")
                .append(address)
                .append(", ")
                .append(place.getCountry())
                .toString();
    }
}
