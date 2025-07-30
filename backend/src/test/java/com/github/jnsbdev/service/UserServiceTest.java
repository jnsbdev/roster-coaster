package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.user.CreateUserDto;
import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    IdService idService = mock(IdService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    UserService userService = new UserService(userRepository, idService, passwordEncoder);

    @Test
    void createUser_shouldSaveUserAndReturnUserDto_whenEmailNotInUse() {
        // Arrange
        CreateUserDto dto = new CreateUserDto("Max", "max@example.com", "123");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("hashed123");
        UUID generatedId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(idService.randomId()).thenReturn(generatedId);

        User savedUser = new User(
                generatedId, "Max", "max@example.com", "hashed123",
                List.of(), List.of()
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.createUser(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(generatedId);
        assertThat(result.name()).isEqualTo("Max");
        assertThat(result.email()).isEqualTo("max@example.com");
    }

    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange
        CreateUserDto dto = new CreateUserDto("Max", "max@example.com", "123");
        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(mock(User.class)));

        // Act + Assert
        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void findUserEntityByEmail_shouldReturnUser_whenFound() {
        // Arrange
        String email = "max@example.com";
        User user = new User(
                UUID.randomUUID(), "Max", email, "pass", List.of(), List.of()
        );
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findUserEntityByEmail(email);

        // Assert
        assertThat(result).isEqualTo(user);
    }

    @Test
    void findUserEntityByEmail_shouldThrowException_whenUserNotFound() {
        // Arrange
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> userService.findUserEntityByEmail(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void findUserDtoByEmail_shouldReturnUserDto_whenUserExists() {
        // Arrange
        String email = "max@example.com";
        User user = new User(
                UUID.randomUUID(), "Max", email, "pass", List.of(), List.of()
        );
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserDto dto = userService.findUserDtoByEmail(email);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.email()).isEqualTo(email);
        assertThat(dto.name()).isEqualTo("Max");
    }

}