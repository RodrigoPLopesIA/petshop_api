package com.petshop.petshop_api.dtos.responses;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ClientResponseDTO(
     String id,
     NameResponseDTO name,
     String phone,
     String email,
     String cpf,
     AddressResponseDTO address,
     String notes,
     LocalDateTime createdAt,
     LocalDateTime updatedAt
) {
    
}
