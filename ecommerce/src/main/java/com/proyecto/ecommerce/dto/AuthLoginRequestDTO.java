package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record AuthLoginRequestDTO(

        @NotBlank(message = "Ingresar un email es obligatorio")
        @Size(min = 10, max = 254, message = "El Email debe tener entre 10 y 254 caracteres")
        @Email
        String email,

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 2, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @NotBlank
        String password)
{
}
