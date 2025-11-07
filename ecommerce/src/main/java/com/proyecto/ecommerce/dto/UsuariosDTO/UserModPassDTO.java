package com.proyecto.ecommerce.dto.UsuariosDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserModPassDTO (

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String passwordOld,
        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String passwordNew
){
}
