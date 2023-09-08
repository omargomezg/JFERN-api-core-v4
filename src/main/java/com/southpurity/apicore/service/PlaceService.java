package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.constant.StatusPlaceEnum;

import java.util.List;
import java.util.Optional;

public interface PlaceService {

    List<PlaceDocument> findAll(StatusPlaceEnum statusPlaceEnum);

    PlaceDocument update(PlaceDocument placeDocument);

    Optional<PlaceDocument> findById(String id);

}
