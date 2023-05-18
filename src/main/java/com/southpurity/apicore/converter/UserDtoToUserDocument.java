package com.southpurity.apicore.converter;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class UserDtoToUserDocument implements Converter<UserDTO, UserDocument> {

    @Override
    public UserDocument convert(UserDTO source) {
        return UserDocument.builder()
                .rut(source.getRut())
                .fullName(source.getFullName())
                .email(source.getEmail())
                .telephone(source.getTelephone())
                .role(source.getRole())
                .updatedDate(new Date())
                .status(source.getStatus())
                .build();
    }
}
