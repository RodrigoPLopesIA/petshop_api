package com.petshop.petshop_api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Name {
    
    private String first_name;
    private String middle_name;
    private String last_name;
}
