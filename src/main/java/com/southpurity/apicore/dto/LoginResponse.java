package com.southpurity.apicore.dto;

import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.model.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {
    private String token;
    private ProfileResponse profile;
}
