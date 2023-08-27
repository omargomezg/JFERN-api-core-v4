package com.southpurity.apicore.dto;

import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import lombok.Data;

@Data
public class ProductFilter {
    private String placeId;
    private OrderStatusEnum status;
}
