package com.proyecto.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    // Maneja validaciones @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // Maneja JSON mal formado o campos faltantes
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        String msg = ex.getMostSpecificCause().getMessage();
        // Detectar si falta un campo obligatorio en un record
        if (msg.contains("apellido")) {
            error.put("apellido", "El apellido es obligatorio");
        } else if (msg.contains("nombre")) {
            error.put("nombre", "El nombre es obligatorio");
        } else if (msg.contains("password")) {
            error.put("password", "La contraseña es obligatoria");
        } else if (msg.contains("email")) {
            error.put("email", "El email es obligatorio o inválido");
        } else {
            error.put("error", "Datos inválidos o incompletos: " + msg);
        }

        return ResponseEntity.badRequest().body(error);
    }

}
