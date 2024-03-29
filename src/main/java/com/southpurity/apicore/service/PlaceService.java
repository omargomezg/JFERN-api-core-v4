package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.constant.StatusPlaceEnum;

import java.util.List;

public interface PlaceService {

    List<PlaceDocument> findAll(StatusPlaceEnum statusPlaceEnum);

    PlaceDocument update(PlaceDocument placeDocument);

}
