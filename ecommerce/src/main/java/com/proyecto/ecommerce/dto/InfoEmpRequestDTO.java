package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.proyecto.ecommerce.model.Contacto;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@JsonPropertyOrder
public record InfoEmpRequestDTO(
        @Pattern(
                regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        String titulo,

        @Pattern(
                regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        String descripcion,

        @Pattern(
                regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        String direccion
        ){

}
