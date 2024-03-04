package com.southpurity.apicore.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ContactRequest {
    private String names;
    private String lastName;
    private String email;
    private String telephone;
    private String message;

    public Map<String, Object> getModel() {
        return Map.of("names", names, "lastName", lastName, "email", email, "telephone", telephone, "message", message);
    }


}
