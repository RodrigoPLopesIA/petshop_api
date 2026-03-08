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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private ClientMapper mapper;

    public Page<ClientResponseDTO> search(ClientFilterDTO filter, Pageable pageable) {
        log.info("Filters: {}", filter);
        Query query = new Query().with(pageable);

        if (filter.name() != null) {
            log.info("Filter name: {}", filter.name());
            query.addCriteria(
                    Criteria.where("name.firstName").regex(filter.name(), "i"));
        }

        if (filter.email() != null) {
            log.info("Filter email: {}", filter.email());
            query.addCriteria(
                    Criteria.where("email").regex(filter.email(), "i"));
        }

        if (filter.phone() != null) {
            log.info("Filter phone: {}", filter.phone());
            query.addCriteria(
                    Criteria.where("phone").is(filter.phone()));
        }

        if (filter.cpf() != null) {
            log.info("Filter cpf: {}", filter.cpf());
            query.addCriteria(
                    Criteria.where("cpf").is(filter.cpf()));
        }

        List<Client> clients = mongoTemplate.find(query, Client.class);
        long total = mongoTemplate.count(
                Query.of(query).limit(-1).skip(-1),
                Client.class);

        log.info("Clients: {}, Total: {}", clients, total);
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