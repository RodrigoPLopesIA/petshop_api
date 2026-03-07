package com.petshop.petshop_api.mapper;

import org.mapstruct.Mapper;

import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.models.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toResponseDTO(Client client);

    Client updateEntityFromDTO(ClientRequestDTO dto, Client client);
}
