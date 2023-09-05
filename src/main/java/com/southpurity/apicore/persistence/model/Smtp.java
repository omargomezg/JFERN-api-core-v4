package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Data;

@Data
public class Smtp {

    @JsonView(View.Administrator.class)
    private String host;

    @JsonView(View.Administrator.class)
    private Integer port;

    @JsonView(View.Administrator.class)
    private String username;

    @JsonView(View.Administrator.class)
    private String password;

    @JsonView(View.Administrator.class)
    private String protocol;

    @JsonView(View.Administrator.class)
    private Boolean auth;

    @JsonView(View.Administrator.class)
    private Boolean starttlsEnable;

    @JsonView(View.Administrator.class)
    private Boolean starttlsRequired;
}
