package com.southpurity.apicore.persistence.model;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@SuperBuilder
public class EventDocument {
    private String title;
    private Date start;
    private Date end;

    @Builder.Default
    private Boolean allDay = false;
    private Byte capacity;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

    @DocumentReference
    private List<UserDocument> clients;
}
