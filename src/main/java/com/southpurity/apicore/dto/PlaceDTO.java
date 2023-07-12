package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {

    @JsonView({
            View.Stocker.class,
            View.Customer.class})
    private String id;

    @JsonView({
            View.Stocker.class,
            View.Customer.class})
    private String country;

    @JsonView({
            View.Stocker.class,
            View.Customer.class})
    private String address;

    /**
     * Padlocks available in place
     */
    @JsonView(View.Administrator.class)
    private int padlocks;

    @JsonView(View.Administrator.class)
    private Short availableStock;

    @JsonView(View.Administrator.class)
    private Date createdDate;
}
