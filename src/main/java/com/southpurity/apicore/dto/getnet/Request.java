package com.southpurity.apicore.dto.getnet;

import lombok.Data;

@Data
public class Request {
    private String locale = "es_CL";
    private Payment payment;
    private String ipAddress;
    private String userAgent;
    private String returnUrl;
}
