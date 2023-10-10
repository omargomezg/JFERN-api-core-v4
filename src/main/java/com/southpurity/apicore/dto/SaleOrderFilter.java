package com.southpurity.apicore.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SaleOrderFilter {
    private Integer page = 0;
    private Integer size = 15;
    private Set<SortDTO> sort = new HashSet<>();
    private String userId;

    public Pageable getPageable() {
        List<Sort.Order> sorts = new ArrayList<>();
        sort.forEach(s -> sorts.add(new Sort.Order(s.getDirection(), s.getProperty())));
        if (sorts.isEmpty()) {
            sorts.add(new Sort.Order(Sort.Direction.DESC, "createdDate"));
        }
        return PageRequest.of(page, size, Sort.by(sorts));
    }

}
