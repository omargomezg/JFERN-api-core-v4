package com.southpurity.apicore.persistence.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Service document that extend from BaseDocument
 */
@Document
@Builder
public class ServiceDocument extends BaseDocument {

    private String shortDescription;
    private String longDescription;

}
