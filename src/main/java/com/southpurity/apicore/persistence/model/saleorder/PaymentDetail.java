package com.southpurity.apicore.persistence.model.saleorder;

import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetail {
    private Integer requestId;
    private String processUrl;
    private String status;
    private String reason;
    private String message;
    private String date;
    private PaymentTypeEnum paymentType;

}
