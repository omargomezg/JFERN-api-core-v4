package com.southpurity.apicore.dto.administrator;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;
import org.bson.types.Decimal128;

@Data
public class ConfigurationDTO {
    @JsonView({View.Administrator.class, View.Customer.class})
    private Decimal128 price;
}
