package com.southpurity.apicore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("places")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDocument {
    @MongoId
    private String id;
    private String country;
    private String address;
}
