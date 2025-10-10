package com.proyecto.ecommerce.dto;

import java.math.BigDecimal;

public record ProductoVentaDTO (
        BigDecimal total,
        int cantidad
){
}
