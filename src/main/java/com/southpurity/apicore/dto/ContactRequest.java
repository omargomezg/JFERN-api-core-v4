package com.southpurity.apicore.dto;

import lombok.Data;

@Data
public class ContactRequest {
    private String names;
    private String lastName;
    private String email;
    private String telephone;
    private String message;
}
