package com.proyecto.ecommerce.dto.ProductosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder
public record ProductoRespDTO(
        Long idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        int stock,
        String iva,
        BigDecimal porcentaje,
        String url,
        boolean Enable
) {
}
