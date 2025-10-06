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
        String nombre,
        String apellido,
        String password,
        String email,
        Long dni,
        LocalDate fechaNac,
        String direccion,
        boolean acceptTerms
){
        public RegisterRequestDTO {
                // ---- Nombre ----
                if (nombre == null || nombre.isBlank()) {
                        throw new IllegalArgumentException("El nombre es obligatorio");
                }
                if (nombre.length() < 5 || nombre.length() > 35) {
                        throw new IllegalArgumentException("El nombre debe tener entre 5 y 35 caracteres");
                }
                if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$")) {
                        throw new IllegalArgumentException("El nombre solo puede contener letras, espacios, guiones y apóstrofes");
                }

                // ---- Apellido ----
                if (apellido == null || apellido.isBlank()) {
                        throw new IllegalArgumentException("El apellido es obligatorio");
                }
                if (apellido.length() < 5 || apellido.length() > 35) {
                        throw new IllegalArgumentException("El apellido debe tener entre 5 y 35 caracteres");
                }
                if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s'-]+$")) {
                        throw new IllegalArgumentException("El apellido solo puede contener letras, espacios, guiones y apóstrofes");
                }

                // ---- Password ----
                if (password == null || password.isBlank()) {
                        throw new IllegalArgumentException("La contraseña es obligatoria");
                }
                if (password.length() < 7 || password.length() > 20) {
                        throw new IllegalArgumentException("La contraseña debe tener entre 7 y 20 caracteres");
                }
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")) {
                        throw new IllegalArgumentException("La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=)");
                }

                // ---- Email ----
                if (email == null || email.isBlank()) {
                        throw new IllegalArgumentException("El email es obligatorio");
                }
                if (email.length() < 10 || email.length() > 254) {
                        throw new IllegalArgumentException("El email debe tener entre 10 y 254 caracteres");
                }
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        throw new IllegalArgumentException("El formato del email no es válido");
                }

                // ---- DNI ----
                if (dni == null) {
                        throw new IllegalArgumentException("El DNI es obligatorio");
                }
                if (dni < 10_000_000L || dni > 99_999_999L) {
                        throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
                }

                // ---- Fecha de Nacimiento ----
                if (fechaNac == null) {
                        throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
                }
                if (fechaNac.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
                }

                // ---- Dirección ----
                if (direccion == null || direccion.isBlank()) {
                        throw new IllegalArgumentException("La dirección es obligatoria");
                }
                if (direccion.length() < 15 || direccion.length() > 120) {
                        throw new IllegalArgumentException("La dirección debe tener entre 15 y 120 caracteres");
                }

                // ---- Terms & Conditions ----
                if (!acceptTerms) {
                        throw new IllegalArgumentException("Debe aceptar los términos y condiciones");
                }
        }
}