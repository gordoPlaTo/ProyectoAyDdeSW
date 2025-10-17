package com.proyecto.ecommerce.dto.ProductosDTO;

import java.math.BigDecimal;

public record ProductoVentaDTO (
        String nombre,
        BigDecimal total,
        Long cantidad,
        BigDecimal ganancia
){
}
