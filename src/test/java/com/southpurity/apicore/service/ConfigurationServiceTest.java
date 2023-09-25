package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @InjectMocks
    ConfigurationService configurationService;

    @Mock
    ConfigurationRepository configurationRepository;

    @Mock
    ModelMapper modelMapper;

    @Test
    void update_success() {
        ConfigurationDocument configurationDocument = ConfigurationDocument.builder()
                .siteName("southpurity")
                .build();
        when(configurationRepository.findBySiteName(anyString())).thenReturn(Optional.of(configurationDocument));
        when(configurationRepository.save(configurationDocument)).thenReturn(configurationDocument);

        var result = configurationService.update(configurationDocument);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(configurationDocument, result);
    }

    @Test
    void update_configuration_not_found() {
        ConfigurationDocument configurationDocument = ConfigurationDocument.builder()
                .siteName("southpurity")
                .build();
        when(configurationRepository.findBySiteName(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> configurationService.update(configurationDocument));
    }
}
