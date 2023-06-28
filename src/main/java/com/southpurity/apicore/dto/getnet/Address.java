package com.southpurity.apicore.dto.getnet;

import lombok.Data;

/**
 * Estructura que contiene la información sobre una dirección física.
 */
@Data
public class Address {

    /**
     * Dirección física completa
     */
    private String street;

    /**
     * Nombre de la ciudad
     */
    private String city;

    /**
     * Nombre del estado coincidente con la dirección
     */
    private String state;

    /**
     * Código postal o equivalente se requiere generalmente para los países que tienen
     */
    private String postalCode;

    /**
     * Código internacional del país que se aplica a la dirección física según ISO 3166-1 ALPHA-2
     */
    private String country;

    /**
     * Número telefónico
     */
    private String phone;
}
