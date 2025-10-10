package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.proyecto.ecommerce.model.IVA;

import java.math.BigDecimal;

@JsonPropertyOrder
public record ProductoRespDTO(
        String nombre,
        String descripcion,
        BigDecimal precio,
        IVA iva
) {
}
