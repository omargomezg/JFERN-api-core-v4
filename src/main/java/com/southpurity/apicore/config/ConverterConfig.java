package com.southpurity.apicore.config;

import com.southpurity.apicore.converter.UserDocumentToUserResponseDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UserDocumentToUserResponseDTO());
    }

}
