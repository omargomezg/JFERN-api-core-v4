package com.southpurity.apicore.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceDTO {

    @JsonView(View.Customer.class)
    private String id;

    @JsonView(View.Customer.class)
    private String shortDescription;

    @JsonView(View.Customer.class)
    private String longDescription;
}
