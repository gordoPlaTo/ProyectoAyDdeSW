package com.proyecto.ecommerce.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalValidationHandler {

    //Aqui captura errores de la anotacion @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new, // para mantener el orden de los campos
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", "Validation failed");
        responseBody.put("errors", fieldErrors);

        return  ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    //Aqui se capturan excepciones genericas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("message", "Error interno del servidor.");
        responseBody.put("errorDetail", ex.getMessage());
        responseBody.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    //Aqui se capturan excepciones de argumentos invalidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Aqui se capturan excepciones de entidad no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Captura errores de constrains
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", "Violación de integridad de datos");
        response.put("errorDetail", ex.getMostSpecificCause().getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Cuerpo Mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "No tiene permisos para acceder a este recurso.");
        response.put("errorDetail", ex.getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Aqui excepciones de usuario no encontrado
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //Aqui excepciones de accedo denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("message", "No tiene permisos para acceder a este recurso. " + ex.getMessage());
        response.put("errors", Collections.emptyList());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", ex.getStatusCode().value());
        body.put("message", ex.getReason());
        body.put("errors", Collections.emptyList());

        return ResponseEntity
                .status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}