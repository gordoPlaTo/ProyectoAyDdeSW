package com.proyecto.ecommerce.dto.authDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public record AuthResponseDTO (String email,
                               String message,
                               String token,
                               boolean status) {
}
