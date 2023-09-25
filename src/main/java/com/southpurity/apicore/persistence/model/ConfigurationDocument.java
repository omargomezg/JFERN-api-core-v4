package com.southpurity.apicore.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.southpurity.apicore.controller.View;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


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
    @NotNull
    @JsonView(View.Anonymous.class)
    private String siteName;

    /**
     * Url de retorno para pago online
     */
    @NotNull
    @JsonView(View.Anonymous.class)
    private String returnUrl;

    /**
     * Valor de recarga
     */
    @JsonView(View.Anonymous.class)
    @Builder.Default
    private Integer price = 2500;

    /**
     * Valor de bidón más recarga
     */
    @JsonView(View.Anonymous.class)
    @Builder.Default
    private Integer priceWithDrum = 7000;

    /**
     * Tiempo en milisegundos para confirmar pago online, por defecto 10 minutos
     */
    @JsonView(View.Anonymous.class)
    @JsonProperty("timeToPay")
    @Builder.Default
    private Long millisecondsToExpirePayment = 600000L;

    @JsonView(View.Administrator.class)
    private Smtp smtp;
}
