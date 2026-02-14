package com.southpurity.apicore;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class ApiCoreApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final PasswordEncoder bcryptEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiCoreApplication.class, args);
    }

    @Bean
    public SimpleModule springDataPageModule() {
        return new SimpleModule().addSerializer(Page.class, new JsonSerializer<Page>() {
            @Override
            public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeNumberField("totalElements", value.getTotalElements());
                gen.writeNumberField("totalPages", value.getTotalPages());
                gen.writeNumberField("number", value.getNumber());
                gen.writeNumberField("size", value.getSize());
                gen.writeBooleanField("first", value.isFirst());
                gen.writeBooleanField("last", value.isLast());
                gen.writeFieldName("content");
                serializers.defaultSerializeValue(value.getContent(), gen);
                gen.writeEndObject();
            }
        });
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("omar.fdo.gomez@gmail.com").isEmpty())
            userRepository.save(UserDocument.builder()
                    .rut("14081226-9")
                    .email("omar.fdo.gomez@gmail.com")
                    .role(RoleEnum.ADMINISTRATOR)
                    .fullName("Omar Gomez")
                    .password(bcryptEncoder.encode("samsungMac"))
                    .build());

        if (configurationRepository.findBySiteName("southpurity").isEmpty()) {
            configurationRepository.save(ConfigurationDocument.builder()
                    .price(2500)
                    .priceWithDrum(7000)
                    .millisecondsToExpirePayment(600000L)
                    .siteName("southpurity")
                    .returnUrl("http://localhost:4200/payment-result")
                    .build());
        }

    }
}
