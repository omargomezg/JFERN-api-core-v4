package com.southpurity.apicore.persistence.model;

import com.southpurity.apicore.persistence.model.constant.PriceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private PriceTypeEnum identifier;
    private Integer amount;
}
