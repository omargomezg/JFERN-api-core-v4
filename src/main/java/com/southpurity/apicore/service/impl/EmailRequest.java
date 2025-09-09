package com.southpurity.apicore.service.impl;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailRequest {
    String to;
    String subject;
    String templateName;
    java.util.Map<String, Object> model;
}
