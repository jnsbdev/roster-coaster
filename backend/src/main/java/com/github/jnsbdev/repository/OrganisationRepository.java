package com.github.jnsbdev.repository;

import com.github.jnsbdev.entity.Organisation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, UUID> {

}
