package com.petshop.petshop_api.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.petshop.petshop_api.config.SecurityConfigTest;
import com.petshop.petshop_api.dtos.requests.AddressRequestDTO;
import com.petshop.petshop_api.dtos.requests.ClientFilterDTO;
import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.requests.NameRequestDTO;
import com.petshop.petshop_api.dtos.responses.AddressResponseDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.dtos.responses.NameResponseDTO;
import com.petshop.petshop_api.services.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(ClientController.class)
@Import(SecurityConfigTest.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService service;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private ClientRequestDTO requestDTO;
    private ClientResponseDTO responseDTO;
    private Jwt mockJwt;

    @BeforeEach
    void setUp() {

        NameRequestDTO nameRequest = new NameRequestDTO("João", "Silva", "Souza");
        AddressRequestDTO addressRequest = new AddressRequestDTO(
                "Rua A", "123", "Apt 1", "Bairro B", "Cidade C", "SP", "12345678"
        );
        requestDTO = new ClientRequestDTO(
                nameRequest,
                "11999999999",
                "teste@email.com",
                "12345678901",
                addressRequest,
                "Observações"
        );

        NameResponseDTO nameResponse = new NameResponseDTO("João", "Silva", "Souza");
        AddressResponseDTO addressResponse = new AddressResponseDTO(
                "Rua A", "123", "Apt 1", "Bairro B", "Cidade C", "SP", "12345678"
        );
        responseDTO = new ClientResponseDTO(
                "1",
                nameResponse,
                "11999999999",
                "teste@email.com",
                "12345678901",
                addressResponse,
                "Observações",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Configurar JWT mock
        mockJwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("scope", "read write")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }

    @Test
    @DisplayName("List all clients")
    void testListClients() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);

        Pageable expectedPageable = PageRequest.of(0, 10);
        Page<ClientResponseDTO> page = new PageImpl<>(List.of(responseDTO), expectedPageable, 1);

        when(service.search(any(ClientFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/clients")
                        .with(jwt()) // Adiciona um token JWT mockado
                        .param("name", "João")
                        .param("email", "teste@email.com")
                        .param("phone", "11999999999")
                        .param("cpf", "12345678901")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].phone").value("11999999999"))
                .andExpect(jsonPath("$.content[0].email").value("teste@email.com"));
    }

    @Test
    @DisplayName("Get client by ID")
    void testGetById() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(service.getById("1")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/clients/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(service, times(1)).getById("1");
    }

    @Test
    @DisplayName("Create client")
    void testCreateClient() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(service.create(any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/clients")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(service, times(1)).create(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Create client with specific authorities")
    void testCreateClientWithAuthorities() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(service.create(any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/clients")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(service, times(1)).create(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Update client")
    void testUpdateClient() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(service.update(eq("1"), any(ClientRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/clients/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.email").value("teste@email.com"));

        verify(service, times(1)).update(eq("1"), any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Delete client")
    void testDeleteClient() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/api/v1/clients/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).delete("1");
    }

    @Test
    @DisplayName("Should return 401 when no token provided")
    void testUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}