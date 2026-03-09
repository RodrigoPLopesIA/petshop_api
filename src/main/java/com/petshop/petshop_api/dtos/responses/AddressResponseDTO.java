package com.petshop.petshop_api.dtos.responses;

import lombok.Builder;

@Builder
public record AddressResponseDTO(
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String zipCode
) {
} 
