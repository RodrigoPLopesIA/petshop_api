package com.petshop.petshop_api.dtos.requests;
import org.checkerframework.checker.units.qual.C;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ClientRequestDTO(

    @NotBlank(message = "O nome é obrigatório")
    NameRequestDTO name,

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
    String phone,

    @Email(message = "O email é inválido")
    String email,

    @Pattern(regexp = "\\d{11}", message = "O CPF é inválido")
    String cpf,

    AddressRequestDTO address,

    String notes
) {}