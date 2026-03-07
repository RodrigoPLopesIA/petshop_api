package com.petshop.petshop_api.dtos.responses;

import java.util.Map;

public record ErrorResponseDTO(String path, String message, int statusCode, Map<String, String> errors) {

    ErrorResponseDTO(String path, String message, int statusCode){
        this(path, message, statusCode, null);
    }
}
