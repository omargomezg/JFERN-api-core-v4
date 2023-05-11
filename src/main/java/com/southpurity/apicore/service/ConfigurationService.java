package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.administrator.ConfigurationDTO;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;
    public ConfigurationDTO get() {
        return  configurationRepository.findAll().stream().map(configuration -> modelMapper.map(configuration, ConfigurationDTO.class))
                .findFirst().orElseThrow();
    }

    public void update(ConfigurationDTO configurationDTO){
        var configuration = configurationRepository.findAll().stream().findFirst().orElseThrow();
        configuration.setPrice(configurationDTO.getPrice());
        configurationRepository.save(configuration);
    }
}
