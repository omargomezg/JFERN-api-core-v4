package com.southpurity.apicore.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MyOrderResponseDTO {
    private String id;
    private String address;

    private Date date;
}
