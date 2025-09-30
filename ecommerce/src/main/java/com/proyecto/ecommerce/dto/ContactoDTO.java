package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public record ContactoDTO (
        String contacto
) {
}
