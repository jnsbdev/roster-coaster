package com.github.jnsbdev.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdService {
    public UUID randomId() {
        return UUID.randomUUID();
    }
}
