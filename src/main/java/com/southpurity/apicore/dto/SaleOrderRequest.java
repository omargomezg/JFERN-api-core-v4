package com.southpurity.apicore.dto;

import lombok.Data;

@Data
public class SaleOrderRequest {
    private Integer page = 0;
    private Integer size = 15;
    private String sortBy = "createdDate";
    private String direction = "desc";
    private String clientId;
}
