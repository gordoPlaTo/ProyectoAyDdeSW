package com.proyecto.ecommerce.dto.PedidosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder
public record PedidoCrearReqDTO(
        List<DetallePedidoReqDTO> listProductos
) {
}
