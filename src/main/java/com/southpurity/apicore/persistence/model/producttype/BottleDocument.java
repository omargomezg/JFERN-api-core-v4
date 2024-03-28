package com.southpurity.apicore.persistence.model.producttype;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.ProductDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Document("productType")
@TypeAlias("bottle")
@Data
public class BottleDocument extends ProductTypeDocument {

    public BottleDocument() {
        super();
    }

    /**
     * Valor de la recarga
     */
    @NotNull
    @JsonView(View.Customer.class)
    private Integer priceRecharge;

    /**
     * Valor del bidón vacío
     */
    @NotNull
    @JsonView(View.Customer.class)
    private Integer priceDrum;

    @DocumentReference(lazy = true, lookup = "{'productType': ?#{#self._id}}")
    private ProductDocument productDocument;

}
