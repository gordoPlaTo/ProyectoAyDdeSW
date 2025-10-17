package com.proyecto.ecommerce.dto.authDTO;


import jakarta.validation.constraints.*;
import java.time.LocalDate;



public record RegisterRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 35, message = "El nombre debe tener entre 3 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$", message = "Solo se permiten letras, espacios, guiones y apóstrofes." )
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 3, max = 35, message = "El apellido debe tener entre 3 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$", message = "Solo se permiten letras, espacios, guiones y apóstrofes." )
        String apellido,

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String password,

        @NotBlank(message = "El email es obligatorio")
        @Size(min = 10, max = 254, message = "El email debe tener entre 10 y 254 caracteres")
        @Email(message = "El formato del email no es válido")
        String email,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener exactamente 8 caracteres a")
        String dni,

        @NotNull(message = "Ingresar la fecha es obligatorio")
        @PastOrPresent(message="La fecha de nacimiento debe ser actual o anterior a este momento")
        LocalDate fechaNac,

        @NotBlank(message = "Ingresar una dirección es obligatorio")
        @Size(min = 15, max = 120, message = "La direccipon debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$" , message = "Solo se permiten letras, espacios, guiones, apóstrofes, comas y dos puntos.")
        String direccion,

        @AssertTrue(message = "Debe aceptar los términos y condiciones")
        boolean acceptTerms
){}