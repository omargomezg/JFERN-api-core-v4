package com.southpurity.apicore;

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
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableMongoAuditing
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
