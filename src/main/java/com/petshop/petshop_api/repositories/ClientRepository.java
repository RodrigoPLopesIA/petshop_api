package com.petshop.petshop_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.petshop.petshop_api.models.Client;

public interface ClientRepository extends MongoRepository<Client, String> {

    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Client> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Client> findByPhone(String phone, Pageable pageable);
}