package com.southpurity.apicore.persistence.model;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("kangooJumps")
@SuperBuilder
public class KangooJumps extends EventDocument {
    private List<String> shoes;
}
