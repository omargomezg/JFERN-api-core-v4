package com.southpurity.apicore.service;

import com.southpurity.apicore.exception.AuthException;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @Mock
    private ConversionService conversionService;

    @Test
    void create_success() {
        var user = mock(UserDocument.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(bcryptEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        var result = jwtUserDetailsService.create(user);
        assertEquals(user, result);
    }

    @Test
    void create_email_already_exists() {
        var user = mock(UserDocument.class);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(AuthException.class, () -> jwtUserDetailsService.create(user));
        assertEquals("Alguien mas usa este email, intente con otro.", exception.getMessage());
    }

}
