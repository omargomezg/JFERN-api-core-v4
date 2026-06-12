package com.southpurity.apicore.service.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VciStatusEnum {
    TSY("Autenticación Exitosa"),
    TSN("Autenticación Rechazada"),
    NP("No Participa, sin autenticación"),
    U3("Falla conexión, Autenticación Rechazada"),
    INV("Datos Inválidos"),
    A("Intentó"),
    CNP1("Comercio no participa"),
    EOP("Error operacional"),
    BNA("BIN no adherido"),
    ENA("Emisor no adherido");

    private final String name;
}
