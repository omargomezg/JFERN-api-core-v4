package com.southpurity.apicore.persistence.model.saleorder;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    @JsonView(View.Administrator.class)
    @Builder.Default
    private Date date = new Date();

    @JsonView(View.Administrator.class)
    private String message;
}
