package com.petshop.petshop_api.services;

import com.petshop.petshop_api.dtos.requests.PetRequestDTO;
import com.petshop.petshop_api.dtos.responses.PetResponseDTO;
import com.petshop.petshop_api.exceptions.NotFoundException;
import com.petshop.petshop_api.mapper.PetMapper;
import com.petshop.petshop_api.models.Pet;
import com.petshop.petshop_api.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository repository;
    private final PetMapper mapper;

    public PetResponseDTO create(PetRequestDTO dto) {

        Pet pet = mapper.toEntity(dto);

        Pet saved = repository.save(pet);

        return mapper.toDTO(saved);
    }

    public Page<PetResponseDTO> findAll(String name, Pageable pageable) {

        Page<Pet> pets;

        if (name != null && !name.isBlank()) {
            pets = repository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            pets = repository.findAll(pageable);
        }

        return pets.map(mapper::toDTO);
    }

    public PetResponseDTO findById(String id) {

        Pet pet = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));

        return mapper.toDTO(pet);
    }

    public List<PetResponseDTO> findByClientId(String clientId) {

        return repository.findByClientId(clientId)
                .stream()
                .map(mapper::toDTO).toList();
    }

    public PetResponseDTO update(String id, PetRequestDTO dto) {

        Pet pet = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));

        mapper.updatePetFromDto(dto, pet);

        Pet updated = repository.save(pet);

        return mapper.toDTO(updated);
    }

    public void delete(String id) {

        Pet pet = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found"));

        repository.delete(pet);
    }
}
