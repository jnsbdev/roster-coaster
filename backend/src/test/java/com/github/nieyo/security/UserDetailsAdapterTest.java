package com.github.nieyo.security;

import com.github.nieyo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserDetailsAdapterTest {

    @Test
    void getAuthorities_shouldReturnRoleAdmin_whenAdminOrganizationIdsNotEmpty() {
        User user = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@example.com",
                "password123",
                List.of(UUID.randomUUID()),
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void getAuthorities_shouldReturnRoleUser_whenAdminOrganizationIdsEmpty() {
        User user = new User(
                UUID.randomUUID(),
                "Bob",
                "bob@example.com",
                "password456",
                List.of(), // adminOrganizationIds empty
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void getAuthorities_shouldReturnRoleUser_whenAdminOrganizationIdsIsNull() {
        User user = new User(
                UUID.randomUUID(),
                "Charlie",
                "charlie@example.com",
                "password789",
                null, // adminOrganizationIds is null
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void getPassword_shouldReturnUserPassword() {
        User user = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@example.com",
                "mypassword",
                List.of(),
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.getPassword()).isEqualTo("mypassword");
    }

    @Test
    void getUsername_shouldReturnUserEmail() {
        User user = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@example.com",
                "mypassword",
                List.of(),
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.getUsername()).isEqualTo("alice@example.com");
    }

    @Test
    void accountFlags_shouldAlwaysReturnTrue() {
        User user = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@example.com",
                "mypassword",
                List.of(),
                List.of()
        );
        UserDetails userDetails = new UserDetailsAdapter(user);

        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }
}
