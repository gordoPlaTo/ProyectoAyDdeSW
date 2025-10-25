package com.proyecto.ecommerce.dto.UsuariosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder
public record UsuarioModReqDTO (

        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String password,

        @Size(min = 15, max = 120, message = "La direccipon debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$" , message = "Solo se permiten letras, espacios, guiones, apóstrofes, comas y dos puntos.")
        String direccion

){
}
