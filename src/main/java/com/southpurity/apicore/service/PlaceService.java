package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.PlaceDTO;
import com.southpurity.apicore.persistence.model.PlaceDocument;

import java.util.List;

public interface PlaceService {

    List<PlaceDTO> findAll();

    List<PlaceDocument> finaAll();

}
