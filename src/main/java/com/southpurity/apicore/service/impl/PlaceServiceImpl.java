package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final ProductRepository productRepository;

    @Override
    public List<PlaceDocument> findAll() {
        var result = placeRepository.findAll();
        result.forEach(place -> {
            var total = productRepository.countProductDocumentByPlace(place);
            place.setPadlocks(total);
        });
        return result;
    }

}
