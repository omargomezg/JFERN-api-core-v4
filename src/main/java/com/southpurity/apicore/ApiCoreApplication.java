package com.southpurity.apicore;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.model.constant.UserStatusEnum;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoAuditing
@RequiredArgsConstructor
public class ApiCoreApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ConfigurationRepository configurationRepository;
    private final PasswordEncoder bcryptEncoder;

    @Value("${configuration.restart-data:false}")
    private boolean populate;

    public static void main(String[] args) {
        SpringApplication.run(ApiCoreApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (populate) {
            userRepository.deleteAll();
            placeRepository.deleteAll();

            userRepository.save(UserDocument.builder()
                    .rut("14081226-9")
                    .email("omar.fdo.gomez@gmail.com")
                    .role(RoleEnum.ADMINISTRATOR)
                    .fullName("Omar Gomez")
                    .password(bcryptEncoder.encode("samsungMac"))
                    .build());

            userRepository.save(UserDocument.builder()
                    .rut("15302615-7")
                    .email("blankitta@gmail.com")
                    .role(RoleEnum.CUSTOMER)
                    .fullName("Blanca Pinot")
                    .password(bcryptEncoder.encode("samsungMac"))
                    .build());

            var place = PlaceDocument.builder()
                    .country("Valdivia")
                    .address("Condominio Los Notros #443").build();
            placeRepository.save(place);
        }

        if (configurationRepository.findAll().isEmpty()) {
            configurationRepository.save(ConfigurationDocument
                    .builder()
                    .price(new Decimal128(2500))
                    .build());
        }
    }
}
