package com.proyecto.ecommerce.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MaterialReqDTO (
        @NotBlank(message = "El nombre del material es obligatorio")
        @Size(min = 5, max = 35, message = "El nombre debe tener entre 5 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$", message = "Solo se permiten letras, espacios, guiones y apóstrofes." )
        String nombre,

        @NotBlank(message = "Ingresar una descripcion es obligatorio")
        @Size(min = 15, max = 300, message = "La descripcion debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$" , message = "Solo se permiten letras, espacios, guiones, apóstrofes, comas y dos puntos.")
        String descripcion,

        @Min(value = 0, message = "El stock no puede ser negativo")
        int stock

        /*Al Modificar:
        aunque se cambie solo uno, deben venir todos los campos completos con los
        datos anteriores que no se modifiquen.*/

) {
}


