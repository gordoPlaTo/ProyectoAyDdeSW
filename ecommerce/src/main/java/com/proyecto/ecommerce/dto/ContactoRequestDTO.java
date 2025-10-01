package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record ContactoRequestDTO(

        @Size(max = 80, message = "El Contacto debe tener maximo 80 caracteres")
        @Pattern(
                regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, espacios, guiones y apóstrofes."
        )
        String contacto
) {
}
