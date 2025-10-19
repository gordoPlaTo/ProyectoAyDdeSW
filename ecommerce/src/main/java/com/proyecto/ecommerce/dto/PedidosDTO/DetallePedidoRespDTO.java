package com.proyecto.ecommerce.dto.PedidosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder
public record DetallePedidoRespDTO(
        Long idDetalle,
        int cantidad,
        BigDecimal precioNeto,
        BigDecimal montoIva,
        BigDecimal precioTotal,
        String producto
) {
}
