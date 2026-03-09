package com.petshop.petshop_api.dtos.responses;

import lombok.Builder;

@Builder
public record NameResponseDTO(String first_name, String middle_name,String last_name) {
    
}
