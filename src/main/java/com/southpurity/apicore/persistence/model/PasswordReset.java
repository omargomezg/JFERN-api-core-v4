package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
public class PasswordReset {
    private Instant requestedAt;

    @JsonView(View.Anonymous.class)
    private String code;

    public boolean isValid(String code) {

        return code.equals(this.code) &&
                ChronoUnit.MINUTES.between(this.requestedAt, Instant.now()) <= 10;
    }
}
