package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.model.constant.UserStatusEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    @JsonView(View.Administrator.class)
    @NotBlank
    private String id;

    @JsonView({View.Stocker.class, View.Customer.class})
    private String rut;

    @JsonView({View.Stocker.class, View.Customer.class})
    private String email;

    private String password;

    @JsonView({View.Stocker.class, View.Customer.class})
    private String telephone;

    @JsonView({View.Stocker.class, View.Customer.class})
    private String fullName;

    @JsonView(View.Customer.class)
    private String fullAddress;

    @Builder.Default
    @JsonView({View.Stocker.class, View.Customer.class})
    private RoleEnum role = RoleEnum.CUSTOMER;

    @Builder.Default
    @JsonView({View.Stocker.class, View.Customer.class})
    private UserStatusEnum status = UserStatusEnum.ACTIVE;
}
