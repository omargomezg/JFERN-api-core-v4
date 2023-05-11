package com.southpurity.apicore.dto.profile;

import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {
    private String id;
    private String rut;
    private String email;
    private String fullName;
    private RoleEnum role;
    private String telephone;
    private String address;
}
