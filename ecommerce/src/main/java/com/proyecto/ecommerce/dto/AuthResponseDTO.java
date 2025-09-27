package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public record AuthResponseDTO (String usuario,
                               String message,
                               String token,
                               boolean status) {
}
