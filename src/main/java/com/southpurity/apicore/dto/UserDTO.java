package com.southpurity.apicore.dto;

import com.southpurity.apicore.model.constant.RoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    private String email;
    private String password;
    private String fullName;
    private String rut;
    private RoleEnum role = RoleEnum.CUSTOMER;
}
