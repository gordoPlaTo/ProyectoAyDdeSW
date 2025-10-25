package com.proyecto.ecommerce.dto.UsuariosDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserModDirecDTO (

        @NotBlank(message = "Ingresar una dirección es obligatorio")
        @Size(min = 15, max = 120, message = "La direccipon debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$" , message = "Solo se permiten letras, espacios, guiones, apóstrofes, comas y dos puntos.")
        String direccion
){
}
