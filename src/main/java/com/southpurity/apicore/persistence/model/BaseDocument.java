package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public abstract class BaseDocument {
    @Id
    @JsonView(View.Administrator.class)
    private String id;

    @JsonView(View.Customer.class)
    @CreatedDate
    private Date createdDate;

    @JsonView(View.Administrator.class)
    @LastModifiedDate
    private Date updatedDate;


}
