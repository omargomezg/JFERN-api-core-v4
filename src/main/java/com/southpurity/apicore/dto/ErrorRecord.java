package com.southpurity.apicore.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;

public record ErrorRecord(@JsonView(View.Anonymous.class) String message) implements View.Anonymous {
}
