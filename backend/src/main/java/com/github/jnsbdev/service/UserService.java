package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.user.CreateUserDto;
import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.mapper.UserMapper;
import com.github.jnsbdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IdService idService;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.password());

        User user = new User(
                idService.randomId(),
                dto.name(),
                dto.email(),
                hashedPassword,
                List.of(),
                List.of()
        );

        User saved = userRepository.save(user);
        return UserMapper.toUserDto(saved);
    }

    public User findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email)
                );
    }

    public UserDto findUserDtoByEmail(String email) {
        User user = findUserEntityByEmail(email);
        return UserMapper.toUserDto(user);
    }

}

