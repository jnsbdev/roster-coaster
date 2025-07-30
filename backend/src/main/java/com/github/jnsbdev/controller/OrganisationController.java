package com.github.jnsbdev.controller;

import com.github.jnsbdev.dto.OrganisationDTO;
import com.github.jnsbdev.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @GetMapping
    public List<OrganisationDTO> getAll() {
        return organisationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganisationDTO> getById(@PathVariable UUID id) {
        return organisationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public OrganisationDTO create(@RequestBody OrganisationDTO organisation) {
        return organisationService.save(organisation);
    }

    @PutMapping("/{id}")
    public OrganisationDTO update(@PathVariable UUID id, @RequestBody OrganisationDTO organisation) {
        // Optional: Prüfen, ob id und organisation.id übereinstimmen
        return organisationService.save(id, organisation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        organisationService.deleteById(id);
    }
}
