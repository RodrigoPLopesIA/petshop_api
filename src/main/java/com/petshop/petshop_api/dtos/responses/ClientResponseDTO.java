package com.petshop.petshop_api.dtos.responses;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ClientResponseDTO(
     String id,
     String name,
     String phone,
     String email,
     String cpf,
     String address,
     String notes,
     LocalDateTime createdAt) {
    
}
