package com.petshop.petshop_api.repositories;
import com.petshop.petshop_api.models.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PetRepository extends MongoRepository<Pet, String> {

    List<Pet> findByClientId(String clientId);

    Page<Pet> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
