package com.proyecto.ecommerce.dto.EmprendimientoDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record InfoEmpRequestDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s',\\-]+$",
                message = "Solo se permiten letras, espacios, guiones, numeros y apóstrofes."
        )
        @Size(max = 20, message = "Se permiten como máximo 20 caracteres en este campo")
        String titulo,

        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, espacios, guiones, numeros y apóstrofes."
        )
        @Size(max = 2500, message = "Se permiten como máximo 2500 caracteres en este campo")
        String descripcion,

        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, espacios, guiones, numeros y apóstrofes."
        )
        @Size(max = 650, message = "Se permiten como máximo 650 caracteres en este campo")
        String direccion,

        @Pattern(
                regexp = "^[0-9]{11}$",
                message = "Solo se permiten numeros para conformar el cuit del emprendimiento"
        )
        @Size(min = 11, max = 11, message = "El CUIT debe tener exactamente 11 dígitos")
        String cuit,

        @NotBlank(message = "El email es obligatorio")
        @Size(min = 10, max = 254, message = "El email debe tener entre 10 y 254 caracteres")
        @Email(message = "El formato del email no es válido")
        String email

        ){

}
