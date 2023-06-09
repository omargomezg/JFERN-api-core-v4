package com.southpurity.apicore.converter;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.core.convert.converter.Converter;

public class UserDocumentToUserResponseDTO implements Converter<UserDocument, UserDTO> {

    @Override
    public UserDTO convert(UserDocument source) {
        return UserDTO.builder()
                .id(source.getId())
                .rut(source.getRut())
                .fullName(source.getFullName())
                .email(source.getEmail())
                .telephone(source.getTelephone())
                .role(source.getRole())
                .status(source.getStatus())
                .build();
    }
}
