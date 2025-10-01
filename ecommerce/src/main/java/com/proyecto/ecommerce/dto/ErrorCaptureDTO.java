package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder
public record ErrorCaptureDTO(
        String error,
        LocalDateTime timestamp
) {
}
