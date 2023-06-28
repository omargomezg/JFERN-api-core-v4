package com.southpurity.apicore.persistence.model.saleorder;

import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ItemDocument {

    private Integer quantity;

    private String name;
    private Integer price;

    @Builder.Default
    private CurrencyEnum money = CurrencyEnum.CLP;
}
