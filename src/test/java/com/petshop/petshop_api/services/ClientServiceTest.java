package com.petshop.petshop_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.petshop.petshop_api.dtos.requests.ClientFilterDTO;
import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.exceptions.NotFoundException;
import com.petshop.petshop_api.mapper.ClientMapper;
import com.petshop.petshop_api.models.Client;
import com.petshop.petshop_api.repositories.ClientRepository;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ClientMapper mapper;

    @InjectMocks
    private ClientService service;

    private Client client;
    private ClientResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        client = new Client();
        responseDTO = mock(ClientResponseDTO.class);
    }

    @Test
    @DisplayName("Should search clients using filter parameters")
    void shouldSearchClients() {

        Pageable pageable = PageRequest.of(0, 10);

        ClientFilterDTO filter = ClientFilterDTO.builder()
                .name("joao")
                .build();

        when(mongoTemplate.find(any(), eq(Client.class)))
                .thenReturn(List.of(client));

        when(mongoTemplate.count(any(), eq(Client.class)))
                .thenReturn(1L);

        when(mapper.toResponseDTO(client))
                .thenReturn(responseDTO);

        Page<ClientResponseDTO> result = service.search(filter, pageable);

        assertEquals(1, result.getTotalElements());
        verify(mongoTemplate).find(any(), eq(Client.class));
    }

    @Test
    @DisplayName("Should return client when ID exists")
    void shouldReturnClientById() {

        when(repository.findById("1"))
                .thenReturn(Optional.of(client));

        when(mapper.toResponseDTO(client))
                .thenReturn(responseDTO);

        ClientResponseDTO result = service.getById("1");

        assertNotNull(result);
        verify(repository).findById("1");
    }

    @Test
    @DisplayName("Should throw NotFoundException when client does not exist")
    void shouldThrowExceptionWhenClientNotFound() {

        when(repository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.getById("1");
        });
    }

    @Test
    @DisplayName("Should create a new client")
    void shouldCreateClient() {

        ClientRequestDTO dto = mock(ClientRequestDTO.class);

        when(mapper.toEntity(dto))
                .thenReturn(client);

        when(repository.save(client))
                .thenReturn(client);

        when(mapper.toResponseDTO(client))
                .thenReturn(responseDTO);

        ClientResponseDTO result = service.create(dto);

        assertNotNull(result);
        verify(repository).save(client);
    }

    @Test
    @DisplayName("Should update existing client")
    void shouldUpdateClient() {

        ClientRequestDTO dto = mock(ClientRequestDTO.class);

        when(repository.findById("1"))
                .thenReturn(Optional.of(client));

        when(repository.save(client))
                .thenReturn(client);

        when(mapper.toResponseDTO(client))
                .thenReturn(responseDTO);

        ClientResponseDTO result = service.update("1", dto);

        assertNotNull(result);

        verify(mapper).updateEntityFromDTO(dto, client);
        verify(repository).save(client);
    }

    @Test
    @DisplayName("Should delete client by ID")
    void shouldDeleteClient() {

        service.delete("1");

        verify(repository).deleteById("1");
    }
}
