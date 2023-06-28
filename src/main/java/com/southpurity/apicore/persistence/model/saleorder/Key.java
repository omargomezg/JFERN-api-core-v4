package com.southpurity.apicore.persistence.model.saleorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Key {

    private String key;

    private String value;
}
