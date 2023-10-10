package com.southpurity.apicore.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class SortDTO {
    private String property;
    private Sort.Direction direction;
}
