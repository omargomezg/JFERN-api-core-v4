package com.southpurity.apicore.dto;

import lombok.Data;

@Data
public class CartRequest {
    private Long quantity;
    private Long price;
    private String description;
    private Integer subtotal;
}
