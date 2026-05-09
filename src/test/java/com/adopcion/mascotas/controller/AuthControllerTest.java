package com.adopcion.mascotas.controller;

import com.adopcion.mascotas.config.JwtUtil;
import com.adopcion.mascotas.model.User;
import com.adopcion.mascotas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        adminUser = new User("admin", "encodedPassword", "admin@test.com", "ADMIN");
        adminUser.setId(1L);

        normalUser = new User("usuario1", "encodedPassword", "user1@test.com", "USER");
        normalUser.setId(2L);
    }

    @Test
    void login_WithValidAdminCredentials_ShouldReturnToken() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "admin123");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.matches("admin123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin")).thenReturn("fake-jwt-token");

        ResponseEntity<Map<String, String>> response = authController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
        verify(userRepository).findByUsername("admin");
        verify(jwtUtil).generateToken("admin");
    }

    @Test
    void login_WithValidUserCredentials_ShouldReturnToken() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "usuario1");
        loginRequest.put("password", "user123");

        when(userRepository.findByUsername("usuario1")).thenReturn(Optional.of(normalUser));
        when(passwordEncoder.matches("user123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("usuario1")).thenReturn("fake-jwt-token");

        ResponseEntity<Map<String, String>> response = authController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
        verify(userRepository).findByUsername("usuario1");
    }

    @Test
    void login_WithInvalidUsername_ShouldReturn401() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "unknown");
        loginRequest.put("password", "password123");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = authController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).containsKey("error");
        verify(userRepository).findByUsername("unknown");
    }

    @Test
    void login_WithWrongPassword_ShouldReturn401() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "wrongpassword");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<Map<String, String>> response = authController.login(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).containsKey("error");
        verify(userRepository).findByUsername("admin");
    }
}