package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonPropertyOrder
public record ErrorCaptureDTO(
        String error,
        HttpStatus status,
        LocalDateTime timestamp
) {
}
