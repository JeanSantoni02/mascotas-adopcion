package com.adopcion.mascotas.service;

import com.adopcion.mascotas.model.User;
import com.adopcion.mascotas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "rawPassword", "test@example.com", "USER");
        user.setId(1L);
    }

    @Test
    void save_ShouldEncodePasswordAndSaveUser() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldReturnNull() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        User result = userService.findByUsername("unknown");

        assertThat(result).isNull();
        verify(userRepository).findByUsername("unknown");
    }

    @Test
    void existsByUsername_WhenUserExists_ShouldReturnTrue() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean result = userService.existsByUsername("testuser");

        assertThat(result).isTrue();
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void existsByUsername_WhenUserDoesNotExist_ShouldReturnFalse() {
        when(userRepository.existsByUsername("unknown")).thenReturn(false);

        boolean result = userService.existsByUsername("unknown");

        assertThat(result).isFalse();
        verify(userRepository).existsByUsername("unknown");
    }
}