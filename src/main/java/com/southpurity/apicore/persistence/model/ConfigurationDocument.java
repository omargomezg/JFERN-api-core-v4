package com.southpurity.apicore.persistence.model;

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
    private String siteName;

    /**
     * Url de retorno para pago online
     */
    private String returnUrl;

    /**
     * Valor de recarga
     */
    private Integer price;

    /**
     * Valor de bidón más recarga
     */
    private Integer priceWithDrum;
}
