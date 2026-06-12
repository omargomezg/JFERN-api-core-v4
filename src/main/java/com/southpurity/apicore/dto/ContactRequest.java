package com.southpurity.apicore.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ContactRequest {
    private String contactName;
    private String email;
    private String telephone;
    private String message;

    public Map<String, Object> getModel() {
        return Map.of("contactName", contactName, "email", email, "telephone", telephone, "message", message);
    }


}
