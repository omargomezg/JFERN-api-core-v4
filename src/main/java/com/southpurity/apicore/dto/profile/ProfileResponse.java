package com.southpurity.apicore.dto.profile;

import com.southpurity.apicore.model.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {
    private String rut;
    private String fullName;
    private RoleEnum role;
}
