package com.southpurity.apicore.persistence.model.producttype;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document("productType")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class ProductTypeDocument extends BaseDocument {

    @NotNull
    @JsonView(View.Customer.class)
    private String shortName;

    @NotNull
    @JsonView(View.Administrator.class)
    private Boolean status = true;
}




