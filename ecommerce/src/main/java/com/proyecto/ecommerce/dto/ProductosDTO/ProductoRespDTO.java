package com.proyecto.ecommerce.dto.ProductosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.proyecto.ecommerce.model.IVA;

import java.math.BigDecimal;

@JsonPropertyOrder
public record ProductoRespDTO(
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        String iva,
        BigDecimal porcentaje,
        String url
) {
}
