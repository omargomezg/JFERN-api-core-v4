package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ProductType extends BaseDocument {

    @JsonView(View.Customer.class)
    private String shortName;

    @JsonView(View.Customer.class)
    private Integer price;

    @JsonView(View.Administrator.class)
    private Boolean status;

}


