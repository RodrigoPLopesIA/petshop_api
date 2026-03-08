package com.petshop.petshop_api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class SecurityConfigTest {

    @Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder decoder = mock(JwtDecoder.class);
        return decoder;
    }
}