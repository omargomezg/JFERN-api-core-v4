package com.southpurity.apicore.dto.profile;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class ProfileResponse {

    @NotNull(message = "id is required")
    @JsonView({View.Customer.class, View.Stocker.class})
    private String id;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String rut;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String email;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String fullName;

    @JsonView({View.Customer.class, View.Stocker.class})
    private RoleEnum role;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String telephone;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String address;

    @JsonView({View.Customer.class, View.Stocker.class})
    private String city;

}
