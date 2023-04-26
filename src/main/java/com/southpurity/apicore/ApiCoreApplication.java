package com.southpurity.apicore;

import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.model.constant.RoleEnum;
import com.southpurity.apicore.repository.PlaceRepository;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiCoreApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;
	private final PasswordEncoder bcryptEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ApiCoreApplication.class, args);
	}

	@Override
	public void run(String... args) {
		userRepository.deleteAll();
		placeRepository.deleteAll();

		var newUser = UserDocument.builder()
				.email("omar.fdo.gomez@gmail.com")
				.role(RoleEnum.ADMINISTRATOR)
				.fullName("Omar Gomez")
				.password(bcryptEncoder.encode("samsungMac"))
				.build();
		userRepository.save(newUser);

		var place = PlaceDocument.builder()
				.country("Valdivia")
				.address("Condominio Los Notros #443").build();
		placeRepository.save(place);

	}
}
