package com.petshop.petshop_api.dtos.requests;


import lombok.Builder;

@Builder
public record ClientFilterDTO(

    String name,
    String email,
    String phone,
    String cpf

) {}
