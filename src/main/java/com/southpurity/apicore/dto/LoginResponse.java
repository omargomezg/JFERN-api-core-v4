package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.dto.profile.ProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {
    @JsonView({View.Stocker.class, View.Customer.class})
    private String token;
    @JsonView({View.Stocker.class, View.Customer.class})
    private ProfileResponse profile;
}
