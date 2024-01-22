package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class History {

    @JsonView(View.Administrator.class)
    @Builder.Default
    private Date date = new Date();

    @JsonView(View.Administrator.class)
    private String message;
}
