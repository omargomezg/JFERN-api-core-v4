package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.StatusPlaceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
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
    @JsonView(View.Anonymous.class)
    private String id;

    @JsonView(View.Anonymous.class)
    private String country;

    @JsonView(View.Anonymous.class)
    private String address;

    @JsonView(View.Administrator.class)
    private Short availableStock;

    @CreatedDate
    @JsonView(View.Administrator.class)
    private Date createdDate;

    @Transient
    @JsonView(View.Administrator.class)
    private Short padlocks;

    @JsonView(View.Administrator.class)
    private StatusPlaceEnum status;
}
