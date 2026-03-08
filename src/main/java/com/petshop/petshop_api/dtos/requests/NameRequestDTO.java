package com.petshop.petshop_api.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record NameRequestDTO(@NotBlank(message = "O primeiro nome é obrigatorio") String first_name, String middle_name, @NotBlank(message = "O ultimo nome é obrigatorio") String last_name) {
    
}
