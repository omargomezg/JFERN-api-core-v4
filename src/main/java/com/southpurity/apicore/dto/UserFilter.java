package com.southpurity.apicore.dto;

import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class UserFilter {
    private String placeId;
    private List<RoleEnum> role;
}
