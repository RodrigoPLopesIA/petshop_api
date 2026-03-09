package com.petshop.petshop_api.dtos.requests;

public record PetRequestDTO(
        String name,
        String species,
        String breed,
        Integer age,
        Double weight,
        String color,
        String notes,
        String clientId
) {}
