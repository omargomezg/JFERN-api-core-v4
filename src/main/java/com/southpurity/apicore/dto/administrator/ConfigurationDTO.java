package com.southpurity.apicore.dto.administrator;

import lombok.Data;
import org.bson.types.Decimal128;

@Data
public class ConfigurationDTO {
    private Decimal128 price;
}
