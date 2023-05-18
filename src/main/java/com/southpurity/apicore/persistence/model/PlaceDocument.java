package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document("place")
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

    @CreatedDate
    private Date createdDate;
}
