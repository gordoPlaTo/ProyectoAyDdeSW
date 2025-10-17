package com.proyecto.ecommerce.dto.MaterialesDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record MaterialesPatchDTO (
        @Size(min = 3, max = 35, message = "El nombre debe tener entre 3 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, números, espacios, guiones y apóstrofes." )
        String nombre,

        @Size(min = 15, max = 300, message = "La descripcion debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, números, espacios, guiones, apóstrofes, comas, puntos, arrobas y paréntesis.")
        String descripcion


){
}
