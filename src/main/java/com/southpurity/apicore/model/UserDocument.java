package com.southpurity.apicore.model;

import com.southpurity.apicore.model.constant.RoleEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document("users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    @DocumentReference
    List<OrderDocument> orders;

    @MongoId
    private String id;

    private RoleEnum role;

    private String rut;

    private String email;

    private String password;

    private String fullName;

}
