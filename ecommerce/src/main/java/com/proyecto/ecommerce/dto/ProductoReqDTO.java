package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@JsonPropertyOrder
public record ProductoReqDTO(

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(min = 3, max = 35, message = "El nombre debe tener entre 3 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, números, espacios, guiones y apóstrofes."
        )
        String nombre,

        @NotBlank(message = "Ingresar una descripcion es obligatorio")
        @Size(min = 15, max = 300, message = "La descripcion debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, números, espacios, guiones, apóstrofes, comas, puntos, arrobas y paréntesis.")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser un valor positivo (mínimo 0.01)")
        @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 dígitos enteros y 2 decimales.")
        BigDecimal precio,

        @Min(value = 0, message = "El stock no puede ser negativo")
        int stock,

        @NotNull(message = "El ID del IVA es obligatorio")
        @Min(value = 1, message = "El ID del IVA debe ser un valor positivo válido")
        Long idIva
) {
}
