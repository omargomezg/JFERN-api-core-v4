package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String name;
    private String photoUrl;
    private String provider;
    @JsonProperty("idToken")
    private String token;
}
