package com.southpurity.apicore.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("configuration")
@Builder
@Data
public class ConfigurationDocument {
    @MongoId
    private String id;
    private Decimal128 price;
}
