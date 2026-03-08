package com.petshop.petshop_api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.petshop.petshop_api.dtos.requests.ClientFilterDTO;
import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.services.ClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    public Page<ClientResponseDTO> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String cpf,
            Pageable pageable) {

        ClientFilterDTO filter = ClientFilterDTO.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .cpf(cpf)
                .build();
        return service.search(filter, pageable);
    }

    @GetMapping("/{id}")
    public ClientResponseDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public ClientResponseDTO create(@RequestBody ClientRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ClientResponseDTO update(
            @PathVariable String id,
            @RequestBody ClientRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
