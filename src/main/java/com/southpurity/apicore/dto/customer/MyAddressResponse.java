package com.southpurity.apicore.dto.customer;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyAddressResponse {

    @JsonView({View.Stocker.class, View.Customer.class})
    private String id;

    @JsonView({View.Stocker.class, View.Customer.class})
    private String fullAddress;

    @JsonView({View.Stocker.class, View.Customer.class})
    private Boolean isPrimary;
}
