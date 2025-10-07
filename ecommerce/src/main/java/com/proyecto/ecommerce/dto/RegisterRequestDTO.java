package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


@JsonPropertyOrder
public record RegisterRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 5, max = 35, message = "El nombre debe tener entre 5 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$", message = "Solo se permiten letras, espacios, guiones y apóstrofes." )
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 5, max = 35, message = "El apellido debe tener entre 5 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$", message = "Solo se permiten letras, espacios, guiones y apóstrofes." )
        String apellido,

        @NotBlank(message = "Ingresar contraseña es obligatorio")
        @Size(min = 7, max = 20, message = "La contraseña debe tener entre 7 y 20 caracteres")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)." )
        String password,

        @NotBlank(message = "Ingresar un email es obligatorio")
        @Size(min = 10, max = 254, message = "El Email debe tener entre 10 y 254 caracteres")
        @Email
        String email,

        @NotNull(message = "Ingresar un email es obligatorio")
        @Digits(integer = 8, fraction = 0, message = "El DNI debe tener 8 dígitos")
        Long dni,

        @NotNull(message = "Ingresar la fecha es obligatorio")
        @PastOrPresent(message="La fecha de nacimiento debe ser actual o anterior a este momento")
        LocalDate fechaNac,

        @NotBlank(message = "Ingresar una dirección es obligatorio")
        @Size(min = 15, max = 120, message = "La direccipon debe tener entre 15 y 120 caracteres")
        String direccion,

        @AssertTrue(message = "Debe aceptar los términos y condiciones")
        boolean acceptTerms
){}