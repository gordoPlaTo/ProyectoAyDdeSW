package com.proyecto.ecommerce.dto.PedidosDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.proyecto.ecommerce.model.DetallePedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonPropertyOrder
public record PedidosClienteDTO (
    Long id,
    LocalDate fechaCreacion,
    BigDecimal totalCompra,
    String urlComprobante,
    List<DetallePedidoRespDTO> detallePedido,
    String nombre,
    String apellido,
    String email,
    String estadoPedido
    ){
}
