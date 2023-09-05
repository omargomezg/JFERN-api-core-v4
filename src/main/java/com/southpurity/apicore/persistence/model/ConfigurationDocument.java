package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Registro de la configuración global de la aplicación
 */
@Document("configuration")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ConfigurationDocument extends BaseDocument {
    /**
     * Nombre del sitio
     */
    @JsonView(View.Anonymous.class)
    private String siteName;

    /**
     * Url de retorno para pago online
     */
    @JsonView(View.Anonymous.class)
    private String returnUrl;

    /**
     * Valor de recarga
     */
    @JsonView(View.Anonymous.class)
    private Integer price;

    /**
     * Valor de bidón más recarga
     */
    @JsonView(View.Anonymous.class)
    private Integer priceWithDrum;

    /**
     * Tiempo en milisegundos para confirmar pago online
     */
    @JsonView(View.Anonymous.class)
    @JsonProperty("timeToPay")
    private Long millisecondsToExpirePayment;

    @JsonView(View.Administrator.class)
    private Smtp smtp;
}
