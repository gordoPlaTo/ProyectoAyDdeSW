package com.proyecto.ecommerce.dto.PedidosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public record DetallePedidoReqDTO(
        Long id,
        int cantidad
) {
}
