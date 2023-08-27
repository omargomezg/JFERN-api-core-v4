package com.southpurity.apicore.dto.payment;

import lombok.Data;

@Data
public class ItemsDto {
    private Integer quantity;
    private Long price;
    private String description;
    private Integer subtotal;
}
