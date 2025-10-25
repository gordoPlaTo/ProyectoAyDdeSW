package com.proyecto.ecommerce.dto.ProductosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder
public record ProductoActRespDTO (
    String nombre,
    String descripcion,
    BigDecimal precio,
    String url){
}
