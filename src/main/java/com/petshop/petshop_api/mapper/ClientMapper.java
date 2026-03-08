package com.petshop.petshop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.petshop.petshop_api.dtos.requests.ClientRequestDTO;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.models.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "address", source = "address")
    @Mapping(target = "name", source = "name")
    Client toEntity(ClientRequestDTO dto);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "name", source = "name")
    ClientResponseDTO toResponseDTO(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "address", source = "address")
    @Mapping(target = "name", source = "name")
    void updateEntityFromDTO(ClientRequestDTO dto, @MappingTarget Client client);
}
