package com.petshop.petshop_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.petshop.petshop_api.dtos.requests.*;
import com.petshop.petshop_api.dtos.responses.ClientResponseDTO;
import com.petshop.petshop_api.models.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "name.first_name", target = "name.first_name")
    @Mapping(source = "name.middle_name", target = "name.middle_name")
    @Mapping(source = "name.last_name", target = "name.last_name")
    @Mapping(source = "address.street", target = "address.street")
    @Mapping(source = "address.number", target = "address.number")
    @Mapping(source = "address.complement", target = "address.complement")
    @Mapping(source = "address.neighborhood", target = "address.neighborhood")
    @Mapping(source = "address.city", target = "address.city")
    @Mapping(source = "address.state", target = "address.state")
    Client toEntity(ClientRequestDTO dto);
    
    @Mapping(source = "name.first_name", target = "name.first_name")
    @Mapping(source = "name.middle_name", target = "name.middle_name")
    @Mapping(source = "name.last_name", target = "name.last_name")
    @Mapping(source = "address.street", target = "address.street")
    @Mapping(source = "address.number", target = "address.number")
    @Mapping(source = "address.complement", target = "address.complement")
    @Mapping(source = "address.neighborhood", target = "address.neighborhood")
    @Mapping(source = "address.city", target = "address.city")
    @Mapping(source = "address.state", target = "address.state")
    @Mapping(source = "address.zipCode", target = "address.zipCode")
    ClientResponseDTO toResponseDTO(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "name.first_name", target = "name.first_name")
    @Mapping(source = "name.middle_name", target = "name.middle_name")
    @Mapping(source = "name.last_name", target = "name.last_name")
    @Mapping(source = "address.street", target = "address.street")
    @Mapping(source = "address.number", target = "address.number")
    @Mapping(source = "address.complement", target = "address.complement")
    @Mapping(source = "address.neighborhood", target = "address.neighborhood")
    @Mapping(source = "address.city", target = "address.city")
    @Mapping(source = "address.state", target = "address.state")
    @Mapping(source = "address.zipCode", target = "address.zipCode")
    void updateEntityFromDTO(ClientRequestDTO dto, @MappingTarget Client client);


}