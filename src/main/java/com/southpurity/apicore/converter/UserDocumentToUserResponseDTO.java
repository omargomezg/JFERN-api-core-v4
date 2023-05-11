package com.southpurity.apicore.converter;

import com.southpurity.apicore.dto.administrator.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.core.convert.converter.Converter;

public class UserDocumentToUserResponseDTO implements Converter<UserDocument, UserDTO> {

    @Override
    public UserDTO convert(UserDocument source) {
        return UserDTO.builder()
                .rut(source.getRut())
                .fullName(source.getFullName())
                .email(source.getEmail())
                .build();
    }
}
