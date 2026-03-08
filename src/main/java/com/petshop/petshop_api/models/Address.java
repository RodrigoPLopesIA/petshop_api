package com.petshop.petshop_api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Address {

    String street;
    String number;
    String complement;
    String neighborhood;
    String city;
    String state;
    String zipCode;
    
}