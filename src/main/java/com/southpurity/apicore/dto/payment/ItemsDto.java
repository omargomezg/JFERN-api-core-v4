package com.southpurity.apicore.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemsDto {
    private Integer quantity;
    private Long price;
    private String description;
    private Integer subtotal;
}
