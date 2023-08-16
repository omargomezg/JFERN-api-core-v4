package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
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

    @JsonView(View.Customer.class)
    private String shortName;

    @DocumentReference(lazy = true)
    @JsonView(View.Customer.class)
    private PlaceDocument place;

    @JsonView(View.Customer.class)
    private String padlockKey;

    @JsonView(View.Customer.class)
    private String lockNumber;

    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.AVAILABLE;
}
