package com.southpurity.apicore.dto;

import lombok.Data;

@Data
public class CartRequest {
    private Integer quantity;
    private Integer price;
    private String description;
    private Integer subtotal;
}
