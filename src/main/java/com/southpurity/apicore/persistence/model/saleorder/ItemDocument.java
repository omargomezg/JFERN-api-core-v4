package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ItemDocument {

    @JsonView(View.Customer.class)
    private Long quantity;

    @JsonView(View.Customer.class)
    private String name;

    @JsonView(View.Customer.class)
    private Long price;

    @Builder.Default
    @JsonView(View.Customer.class)
    private CurrencyEnum money = CurrencyEnum.CLP;
}
