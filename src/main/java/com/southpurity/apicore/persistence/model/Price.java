package com.southpurity.apicore.persistence.model;

import com.southpurity.apicore.persistence.model.constant.PriceTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {
    private PriceTypeEnum identifier;
    private Integer amount;
}
