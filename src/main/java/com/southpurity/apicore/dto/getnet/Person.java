package com.southpurity.apicore.dto.getnet;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Person {
    /**
     * Tipo de identificación del pagador
     */
    private String documentType;

    /**
     * Identificación
     */
    private String document;

    /**
     * Nombres de la persona
     */
    private String name;

    /**
     * Apellidos de la persona
     */
    private String surname;

    /**
     * Nombre de la empresa en que trabaja o representa
     */
    private String company;
    private String email;

    /**
     * Información completa de la dirección
     */
    private Address address;

    @Size(max = 30)
    private String mobile;
}
