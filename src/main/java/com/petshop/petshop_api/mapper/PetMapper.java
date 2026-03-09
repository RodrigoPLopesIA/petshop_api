package com.petshop.petshop_api.mapper;

import com.petshop.petshop_api.dtos.requests.PetRequestDTO;
import com.petshop.petshop_api.dtos.responses.PetResponseDTO;
import com.petshop.petshop_api.models.Pet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PetMapper {

    Pet toEntity(PetRequestDTO dto);

    PetResponseDTO toDTO(Pet pet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePetFromDto(PetRequestDTO dto, @MappingTarget Pet pet);
}