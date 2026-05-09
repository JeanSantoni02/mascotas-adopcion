package com.adopcion.mascotas.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "MiClaveSuperSecretaParaJWT12345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtUtil.generateToken("testuser");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken("testuser");
        String username = jwtUtil.extractUsername(token);
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken("testuser");
        boolean isValid = jwtUtil.validateToken(token);
        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        boolean isValid = jwtUtil.validateToken("invalid.token.here");
        assertThat(isValid).isFalse();
    }
}