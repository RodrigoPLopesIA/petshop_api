package com.petshop.petshop_api.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.petshop.petshop_api.dtos.responses.ErrorResponseDTO;
import com.petshop.petshop_api.exceptions.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
@ControllerAdvice
public class ControllerAdviceConfig {
    

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFoundException(NotFoundException ex, HttpServletRequest request) {
        var NOT_FOUND = 404;
        return ResponseEntity.status(NOT_FOUND).body(ErrorResponseDTO.builder().path(request.getPathInfo()).message(ex.getMessage()).statusCode(NOT_FOUND).build());
    }

}
