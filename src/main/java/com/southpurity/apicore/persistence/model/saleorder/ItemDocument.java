package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@Builder
public class ItemDocument {

    @NotNull
    @JsonView(View.Customer.class)
    private Long quantity;

    @JsonView(View.Customer.class)
    private String name;

    @NotNull
    @JsonView(View.Customer.class)
    private Long price;

    @Builder.Default
    @JsonView(View.Customer.class)
    private CurrencyEnum money = CurrencyEnum.CLP;
}
