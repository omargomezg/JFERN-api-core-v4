package com.southpurity.apicore.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {
    @MongoId
    private ObjectId id;

    private String email;

    private String password;

}
