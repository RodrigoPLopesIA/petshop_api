package com.petshop.petshop_api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.petshop.petshop_api.dtos.requests.AddressRequestDTO;
import com.petshop.petshop_api.dtos.requests.NameRequestDTO;
import com.petshop.petshop_api.dtos.responses.AddressResponseDTO;
import com.petshop.petshop_api.dtos.responses.NameResponseDTO;
import com.petshop.petshop_api.models.Address;
import com.petshop.petshop_api.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

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
    private ClientRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        // Configurar objetos simples, sem builder pra evitar problemas
        client = new Client();
        client.setId("1");

        responseDTO = new ClientResponseDTO("1", null, "11999999999", "joao@email.com",
                "12345678901", null, "Cliente regular", LocalDateTime.now(), LocalDateTime.now());

        requestDTO = new ClientRequestDTO(null, "11999999999", "joao@email.com",
                "12345678901", null, "Cliente regular");
    }

    @Test
    @DisplayName("Should search clients using filter parameters")
    void shouldSearchClients() {
        Pageable pageable = PageRequest.of(0, 10);
        ClientFilterDTO filter = ClientFilterDTO.builder()
                .name("joao")
                .build();

        List<Client> clients = List.of(client);

        when(mongoTemplate.find(any(Query.class), eq(Client.class))).thenReturn(clients);
        when(mongoTemplate.count(any(Query.class), eq(Client.class))).thenReturn(1L);
        when(mapper.toResponseDTO(client)).thenReturn(responseDTO);

        Page<ClientResponseDTO> result = service.search(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(mongoTemplate).find(any(Query.class), eq(Client.class));
        verify(mongoTemplate).count(any(Query.class), eq(Client.class));
        verify(mapper).toResponseDTO(client);
    }

    @Test
    @DisplayName("Should return empty page when no clients found")
    void shouldReturnEmptyPageWhenNoClientsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        ClientFilterDTO filter = ClientFilterDTO.builder().name("joao").build();

        when(mongoTemplate.find(any(Query.class), eq(Client.class))).thenReturn(List.of());
        when(mongoTemplate.count(any(Query.class), eq(Client.class))).thenReturn(0L);

        Page<ClientResponseDTO> result = service.search(filter, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(Client.class));
        verify(mongoTemplate).count(any(Query.class), eq(Client.class));
    }

    @Test
    @DisplayName("Should return client when ID exists")
    void shouldReturnClientById() {
        when(repository.findById("1")).thenReturn(Optional.of(client));
        when(mapper.toResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO result = service.getById("1");

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(repository).findById("1");
        verify(mapper).toResponseDTO(client);
    }

    @Test
    @DisplayName("Should throw NotFoundException when client does not exist")
    void shouldThrowExceptionWhenClientNotFound() {
        String nonExistentId = "999";
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.getById(nonExistentId));

        assertEquals( exception.getMessage(), "Client not found");
        verify(repository).findById(nonExistentId);
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Should create a new client")
    void shouldCreateClient() {
        when(mapper.toEntity(requestDTO)).thenReturn(client);
        when(repository.save(client)).thenReturn(client);
        when(mapper.toResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO result = service.create(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(mapper).toEntity(requestDTO);
        verify(repository).save(client);
        verify(mapper).toResponseDTO(client);
    }

    @Test
    @DisplayName("Should update existing client")
    void shouldUpdateClient() {
        when(repository.findById("1")).thenReturn(Optional.of(client));
        when(repository.save(client)).thenReturn(client);
        when(mapper.toResponseDTO(client)).thenReturn(responseDTO);

        ClientResponseDTO result = service.update("1", requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(repository).findById("1");
        verify(mapper).updateEntityFromDTO(requestDTO, client);
        verify(repository).save(client);
        verify(mapper).toResponseDTO(client);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent client")
    void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        String nonExistentId = "999";
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.update(nonExistentId, requestDTO));

        assertEquals( exception.getMessage(), "Client not found");
        verify(repository).findById(nonExistentId);
        verify(mapper, never()).updateEntityFromDTO(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete client by ID")
    void shouldDeleteClient() {
        String clientId = "1";
        doNothing().when(repository).deleteById(clientId);

        service.delete(clientId);

        verify(repository).deleteById(clientId);
    }

    @Test
    @DisplayName("Should search with empty filter")
    void shouldSearchWithEmptyFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        ClientFilterDTO filter = ClientFilterDTO.builder().build();

        List<Client> clients = List.of(client);

        when(mongoTemplate.find(any(Query.class), eq(Client.class))).thenReturn(clients);
        when(mongoTemplate.count(any(Query.class), eq(Client.class))).thenReturn(1L);
        when(mapper.toResponseDTO(client)).thenReturn(responseDTO);

        Page<ClientResponseDTO> result = service.search(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(mongoTemplate).find(any(Query.class), eq(Client.class));
        verify(mongoTemplate).count(any(Query.class), eq(Client.class));
    }
}