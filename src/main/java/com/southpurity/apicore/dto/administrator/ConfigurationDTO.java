package com.southpurity.apicore.dto.administrator;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

@Data
public class ConfigurationDTO {
    @JsonView({View.Administrator.class, View.Customer.class})
    private Integer price;

    @JsonView({View.Administrator.class, View.Customer.class})
    private Integer priceWithDrum;

    @JsonView(View.Administrator.class)
    private String returnUrl;

}
