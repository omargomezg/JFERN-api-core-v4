package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import com.southpurity.apicore.persistence.model.BaseDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("saleOrder")
public class SaleOrderDocument extends BaseDocument {

    @Builder.Default
    @JsonView(View.Customer.class)
    private Long serial = new Date().getTime();

    @Builder.Default
    @JsonView(View.Customer.class)
    List<ItemDocument> items = new ArrayList<>();

    @JsonView(View.Customer.class)
    @Builder.Default
    private SaleOrderStatusEnum status = SaleOrderStatusEnum.PENDING;

    @JsonView(View.Customer.class)
    private PaymentDetail paymentDetail;

    @DocumentReference
    private UserDocument client;

    @DocumentReference
    private Collection<ProductDocument> products;

    @JsonView(View.Customer.class)
    @Builder.Default
    private Collection<Key> keys = new ArrayList<>();

    @JsonView(View.Customer.class)
    @Transient
    private Long total;
}
