package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;

@JsonPropertyOrder
public record AuthLoginRequestDTO(

        @NotBlank(message = "Ingresar un email es obligatorio")
        @Size(min = 10, max = 254, message = "El Email debe tener entre 10 y 254 caracteres")
        @Email(message = "El formato del correo electrónico es inválido. Debe incluir '@' y un dominio válido (ej: usuario@ejemplo.com)")
        String email,

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String password
)
{
}
