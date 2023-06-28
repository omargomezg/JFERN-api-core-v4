package com.southpurity.apicore.dto.getnet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetnetRequest {

    @Builder.Default
    private String locale = "es_CL";

    private Payment payment;
    private String ipAddress;
    private String userAgent;
    private String returnUrl;
}
