package com.proyecto.ecommerce.dto.EmprendimientoDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record InfoEmpRequestDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s',\\-]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        @Size(max = 20, message = "Se permiten como máximo 20 caracteres en este campo")
        String titulo,

        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        @Size(max = 120, message = "Se permiten como máximo 120 caracteres en este campo")
        String descripcion,

        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        @Size(max = 120, message = "Se permiten como máximo 120 caracteres en este campo")
        String direccion
        ){

}
