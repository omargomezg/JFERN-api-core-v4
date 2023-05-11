package com.southpurity.apicore.dto.administrator;

import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.model.constant.UserStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private String id;
    private final String rut;
    private final String fullName;
    private final String telephone;
    private final String email;
    private final RoleEnum role;
    private final UserStatusEnum status;
    private final String fullAddress;
}
