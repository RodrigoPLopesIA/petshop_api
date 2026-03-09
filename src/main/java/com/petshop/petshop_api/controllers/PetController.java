package com.petshop.petshop_api.controllers;

import com.petshop.petshop_api.dtos.requests.PetRequestDTO;
import com.petshop.petshop_api.dtos.responses.PetResponseDTO;
import com.petshop.petshop_api.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiv/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService service;

    @PostMapping
    public PetResponseDTO create(@RequestBody PetRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public Page<PetResponseDTO> findAll(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return service.findAll(name, pageable);
    }

    @GetMapping("/{id}")
    public PetResponseDTO findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<PetResponseDTO> findByClient(@PathVariable String clientId) {
        return service.findByClientId(clientId);
    }

    @PutMapping("/{id}")
    public PetResponseDTO update(
            @PathVariable String id,
            @RequestBody PetRequestDTO dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
