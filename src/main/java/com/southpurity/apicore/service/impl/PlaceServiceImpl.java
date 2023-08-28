package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.constant.StatusPlaceEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final MongoTemplate mongoTemplate;


    @Override
    public List<PlaceDocument> findAll(StatusPlaceEnum statusPlaceEnum) {
        Query query = new Query();
        if (statusPlaceEnum != null) {
            query.addCriteria(Criteria.where("status").is(statusPlaceEnum));
        }

        var places = mongoTemplate.find(query, PlaceDocument.class);
        places.forEach(place -> {
            var total = productRepository.countProductDocumentByPlace(place);
            place.setPadlocks(total);
        });
        return places;
    }

    @Override
    public PlaceDocument update(PlaceDocument placeDocument) {
        return placeRepository.save(placeDocument);
    }

}
