package com.petshop.petshop_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.petshop.petshop_api.dtos.requests.ClientFilterDTO;
import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.exceptions.NotFoundException;
import com.petshop.petshop_api.mapper.ClientMapper;
import com.petshop.petshop_api.models.Client;
import com.petshop.petshop_api.repositories.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private ClientMapper mapper;

    public Page<ClientResponseDTO> search(ClientFilterDTO filter, Pageable pageable) {

        Query query = new Query().with(pageable);

        if (filter.name() != null) {
            query.addCriteria(
                    Criteria.where("name.firstName").regex(filter.name(), "i"));
        }

        if (filter.email() != null) {
            query.addCriteria(
                    Criteria.where("email").regex(filter.email(), "i"));
        }

        if (filter.phone() != null) {
            query.addCriteria(
                    Criteria.where("phone").is(filter.phone()));
        }

        if (filter.cpf() != null) {
            query.addCriteria(
                    Criteria.where("cpf").is(filter.cpf()));
        }

        List<Client> clients = mongoTemplate.find(query, Client.class);

        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                Client.class);

        return new PageImpl<>(
                clients.stream().map(mapper::toResponseDTO).toList(),
                pageable,
                total);
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
                .orElseThrow(() -> new NotFoundException("Client not found"));

        return mapper.toResponseDTO(client);
    }

    public ClientResponseDTO create(ClientRequestDTO dto) {

        Client client = mapper.toEntity(dto);

        return mapper.toResponseDTO(repository.save(client));
    }

    public ClientResponseDTO update(String id, ClientRequestDTO dto) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        mapper.updateEntityFromDTO(dto, client);

        return mapper.toResponseDTO(repository.save(client));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}