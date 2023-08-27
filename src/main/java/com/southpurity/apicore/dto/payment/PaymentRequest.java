package com.southpurity.apicore.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private PlaceDto place;
    private Date createdDate;
    private List<ItemsDto> items = new ArrayList<>();
    private ClientDto client = new ClientDto();
    private List<ProductDto> products = new ArrayList<>();
    private String paymentType;
    private String ipAddress;
    private String userAgent;
}
