package com.southpurity.apicore.dto.administrator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String id;
    private String rut;
    private String fullName;
    private String place;
}
