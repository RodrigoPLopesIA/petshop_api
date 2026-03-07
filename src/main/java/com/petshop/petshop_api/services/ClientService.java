package com.petshop.petshop_api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.exceptions.BusinessException;
import com.petshop.petshop_api.mapper.ClientMapper;
import com.petshop.petshop_api.models.Client;
import com.petshop.petshop_api.repositories.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    @Autowired
    private ClientMapper mapper;

    public Page<ClientResponseDTO> list(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponseDTO);
    }

    public Page<ClientResponseDTO> searchByName(Pageable pageable, String name) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toResponseDTO);
    }

    public Page<ClientResponseDTO> searchByEmail(Pageable pageable, String email) {
        return repository.findByEmailContainingIgnoreCase(email, pageable)
                .map(mapper::toResponseDTO);
    }

    public Page<ClientResponseDTO> searchByPhone(Pageable pageable, String phone) {
        return repository.findByPhone(phone, pageable)
                .map(mapper::toResponseDTO);
    }

    public ClientResponseDTO getById(String id) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Client not found"));

        return mapper.toResponseDTO(client);
    }

    public ClientResponseDTO create(ClientRequestDTO dto) {

        Client client = mapper.toEntity(dto);

        return mapper.toResponseDTO(repository.save(client));
    }

    public ClientResponseDTO update(String id, ClientRequestDTO dto) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Client not found"));

        mapper.updateEntityFromDTO(dto, client);

        return mapper.toResponseDTO(repository.save(client));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}