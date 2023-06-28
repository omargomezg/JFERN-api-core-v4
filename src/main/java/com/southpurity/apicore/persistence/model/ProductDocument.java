package com.southpurity.apicore.persistence.model;

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

    private String shortName;

    @DocumentReference
    private PlaceDocument place;

    private String padlockKey;

    private String lockNumber;

    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.AVAILABLE;
}
