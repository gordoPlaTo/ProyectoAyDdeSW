package com.proyecto.ecommerce.dto.EmprendimientoDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record ContactoRequestDTO(

        @Size(max = 80, message = "El Contacto debe tener máximo 80 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, números y los caracteres especiales: @ . _ - = , ( ) '"
        )
        String contacto
) {
}
