package com.southpurity.apicore;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.KangooJumps;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.KangooJumpsRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
@RequiredArgsConstructor
public class ApiCoreApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ConfigurationRepository configurationRepository;
    private final PasswordEncoder bcryptEncoder;
    private final KangooJumpsRepository kangooJumpsRepository;
    private final SaleOrderRepository saleOrderRepository;

    @Value("${configuration.restart-data:false}")
    private boolean populate;

    public static void main(String[] args) {
        SpringApplication.run(ApiCoreApplication.class, args);
    }

    @Bean
    public SimpleModule springDataPageModule() {
        return new SimpleModule().addSerializer(Page.class, new JsonSerializer<Page>() {
            @Override
            public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeNumberField("totalElements",value.getTotalElements());
                gen.writeNumberField("totalPages", value.getTotalPages());
                gen.writeNumberField("number", value.getNumber());
                gen.writeNumberField("size", value.getSize());
                gen.writeBooleanField("first", value.isFirst());
                gen.writeBooleanField("last", value.isLast());
                gen.writeFieldName("content");
                serializers.defaultSerializeValue(value.getContent(),gen);
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

        if (populate) {
            userRepository.deleteAll();
            placeRepository.deleteAll();


            userRepository.save(UserDocument.builder()
                    .rut("15302615-7")
                    .email("blankitta@gmail.com")
                    .role(RoleEnum.CUSTOMER)
                    .fullName("Blanca Pinot")
                    .password(bcryptEncoder.encode("samsungMac"))
                    .build());

            userRepository.save(UserDocument.builder()
                    .rut("99999999-9")
                    .email("omar.gomez@outlook.com")
                    .role(RoleEnum.CUSTOMER)
                    .fullName("Blanca Pinot")
                    .password(bcryptEncoder.encode("samsungMac"))
                    .build());

            var place = PlaceDocument.builder()
                    .country("Valdivia")
                    .address("Condominio Los Notros #443").build();
            placeRepository.save(place);
        }

        if (configurationRepository.findBySiteName("southpurity").isEmpty()) {
            configurationRepository.save(ConfigurationDocument.builder()
                    .price(2500)
                    .siteName("southpurity")
                    .returnUrl("http://localhost:4200/cliente/payment-result")
                    .build());
        }

        Date start = new Date();
        //create end and add 1 hour to start date
        Date end = new Date(start.getTime() + 3600000);

        KangooJumps service = KangooJumps.builder()
                .shoes(List.of("M", "L", "XL", "M"))
                .start(start)
                .end(end)
                .capacity((byte) 5)
                .build();
        kangooJumpsRepository.save(service);

    }
}
