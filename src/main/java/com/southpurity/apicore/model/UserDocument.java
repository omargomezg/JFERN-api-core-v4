package com.southpurity.apicore.model;

import com.southpurity.apicore.model.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document("users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    @DocumentReference
    private List<OrderDocument> orders;

    private List<AddressDocument> addresses = new ArrayList<>();

    @MongoId
    private String id;

    private RoleEnum role;

    private String rut;

    private String email;

    private String password;

    private String fullName;

}
