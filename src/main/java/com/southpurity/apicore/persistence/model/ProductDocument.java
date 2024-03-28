package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.model.producttype.ProductTypeDocument;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("products")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ProductDocument extends BaseDocument {

    @DocumentReference(lazy = true)
    @JsonView(View.Anonymous.class)
    private PlaceDocument place;

    @DocumentReference(lazy = true)
    @JsonView(View.Anonymous.class)
    private BottleDocument productType;

    @JsonView(View.Customer.class)
    private String padlockKey;

    @JsonView(View.Customer.class)
    private String lockNumber;

    @Builder.Default
    @JsonView(View.Administrator.class)
    private OrderStatusEnum status = OrderStatusEnum.AVAILABLE;
}
