package com.petshop.petshop_api.dtos.responses;

import java.util.Date;

public record PetResponseDTO(
        String id,
        String name,
        String species,
        String breed,
        Integer age,
        Double weight,
        String color,
        String notes,
        String clientId,
        Date createdAt,
        Date updatedAt
) {}
