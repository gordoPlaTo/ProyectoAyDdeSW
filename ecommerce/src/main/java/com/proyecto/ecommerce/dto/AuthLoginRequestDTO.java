package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record AuthLoginRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String usuario,

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 2, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @NotBlank String password){


}
