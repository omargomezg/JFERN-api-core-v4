package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDetail {
    @JsonView(View.Administrator.class)
    private Integer requestId;

    @JsonView(View.Administrator.class)
    private String processUrl;

    @JsonView(View.Administrator.class)
    private String status;

    @JsonView(View.Administrator.class)
    private String reason;

    @JsonView(View.Administrator.class)
    private String message;

    @JsonView(View.Administrator.class)
    private String date;

    @JsonView(View.Administrator.class)
    private PaymentTypeEnum paymentType;


}
