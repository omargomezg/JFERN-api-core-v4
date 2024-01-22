package com.southpurity.apicore.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class SaleOrderRequest {
    private Integer page = 0;
    private Integer size = 15;
    private String sortBy = "createdDate";
    private Sort.Direction direction = Sort.Direction.DESC;
    private String userId;
}
