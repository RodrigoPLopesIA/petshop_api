package com.petshop.petshop_api.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.List;

import com.petshop.petshop_api.dtos.requests.AddressRequestDTO;
import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.requests.NameRequestDTO;
import com.petshop.petshop_api.dtos.responses.AddressResponseDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.dtos.responses.NameResponseDTO;
import com.petshop.petshop_api.services.ClientService;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ClientControllerTest {

    @Mock
    private ClientService service;

    @InjectMocks
    private ClientController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ClientRequestDTO requestDTO;
    private ClientResponseDTO responseDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        NameRequestDTO nameRequest = new NameRequestDTO("João", "Silva", "Souza");
        AddressRequestDTO addressRequest = new AddressRequestDTO("Rua A", "123", "Apt 1", "Bairro B", "Cidade C", "SP", "12345678");
        requestDTO = new ClientRequestDTO(nameRequest, "11999999999", "teste@email.com", "12345678901", addressRequest, "Observações");

        NameResponseDTO nameResponse = new NameResponseDTO("João", "Silva", "Souza");
        AddressResponseDTO addressResponse = new AddressResponseDTO("Rua A", "123", "Apt 1", "Bairro B", "Cidade C", "SP", "12345678");
        responseDTO = new ClientResponseDTO("1", nameResponse, "11999999999", "teste@email.com",
                "12345678901", addressResponse, "Observações", LocalDateTime.now(), LocalDateTime.now());

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("List all clients")
    void testListClients() throws Exception {
        when(service.search(any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(responseDTO), pageable, 1));

        mockMvc.perform(get("/api/v1/clients")
                        .param("name", "João")
                        .param("email", "teste@email.com")
                        .param("phone", "11999999999")
                        .param("cpf", "12345678901")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name.firstName").value("João"))
                .andExpect(jsonPath("$.content[0].email").value("teste@email.com"));

        verify(service, times(1)).search(any(), eq(pageable));
    }

    @Test
    @DisplayName("Get client by ID")
    void testGetById() throws Exception {
        when(service.getById("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phone").value("11999999999"));

        verify(service, times(1)).getById("1");
    }

    @Test
    @DisplayName("Create client")
    void testCreateClient() throws Exception {
        when(service.create(any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name.firstName").value("João"));

        verify(service, times(1)).create(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Update client")
    void testUpdateClient() throws Exception {
        when(service.update(eq("1"), any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name.firstName").value("João"));

        verify(service, times(1)).update(eq("1"), any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Delete client")
    void testDeleteClient() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).delete("1");
    }
}