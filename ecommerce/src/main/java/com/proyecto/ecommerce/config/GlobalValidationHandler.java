package com.proyecto.ecommerce.config;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalValidationHandler {

    //Captura los errores producidos al validar datos y los mapea a una estructura mas apta
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

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

        return responseBody;
    }

    //Captura todos los errores y mapea una respuesta mas estructurada
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleAllExceptions(Exception ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseBody.put("message", ex.getMessage());
        responseBody.put("errors", Collections.emptyList());
        return responseBody;
    }
}