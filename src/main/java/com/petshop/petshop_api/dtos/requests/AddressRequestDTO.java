package com.petshop.petshop_api.dtos.requests;

public record AddressRequestDTO( 
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String zipCode
) {
    
}
