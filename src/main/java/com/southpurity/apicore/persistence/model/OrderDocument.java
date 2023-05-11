package com.southpurity.apicore.persistence.model;

import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document("order")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {
    @MongoId
    private String id;

    @DocumentReference
    private PlaceDocument place;
    private Integer padlockKey;
    private Integer lockNumber;

    @CreatedDate
    private Date createdDate;

    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.AVAILABLE;
}
