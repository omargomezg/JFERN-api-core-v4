package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.HashSet;
import java.util.Set;

@Document("products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDocument extends BaseDocument {

    @JsonView(View.Anonymous.class)
    private String shortName;

    @JsonView(View.Anonymous.class)
    private Integer price;

    @JsonView(View.Anonymous.class)
    private Integer priceWithDrum;

    @DocumentReference(lazy = true)
    @JsonView(View.Anonymous.class)
    private PlaceDocument place;

    @Builder.Default
    @JsonView(View.Anonymous.class)
    Set<Price> prices = new HashSet<>();

    @JsonView({ View.Customer.class, View.Stocker.class })
    private String padlockKey;

    @JsonView({ View.Customer.class, View.Stocker.class })
    private String lockNumber;

    @Builder.Default
    @JsonView({ View.Administrator.class, View.Stocker.class })
    private OrderStatusEnum status = OrderStatusEnum.AVAILABLE;
}
