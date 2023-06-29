package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Key {

    @JsonView(View.Customer.class)
    private String key;

    @JsonView(View.Customer.class)
    private String value;
}
