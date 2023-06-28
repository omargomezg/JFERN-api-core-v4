package com.southpurity.apicore.persistence.model.saleorder;

import lombok.Data;

@Data
public class PaymentDetail {
    private Integer requestId;
    private String processUrl;
    private String status;
    private String reason;
    private String message;
    private String date;

}
