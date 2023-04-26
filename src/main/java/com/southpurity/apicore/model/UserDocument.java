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
    private String email;
    private String password;
    private String fullName;

}
