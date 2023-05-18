package com.southpurity.apicore.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageDTO<T> extends PageImpl<T> {
    public PageDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageDTO(List<T> content) {
        super(content);
    }

    @JsonView(View.Administrator.class)
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @JsonView(View.Administrator.class)
    public long getTotalElements() {
        return super.getTotalElements();
    }

    @JsonView(View.Administrator.class)
    public boolean hasNext() {
        return super.hasNext();
    }

    @JsonView(View.Administrator.class)
    public boolean isLast() {
        return super.isLast();
    }

    @JsonView(View.Administrator.class)
    public boolean hasContent() {
        return super.hasContent();
    }

    @JsonView(View.Administrator.class)
    public List<T> getContent() {
        return super.getContent();
    }
}
