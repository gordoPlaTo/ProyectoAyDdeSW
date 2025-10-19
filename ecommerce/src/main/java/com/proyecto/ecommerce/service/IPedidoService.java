package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.PedidosDTO.ComprobanteReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidosClienteDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Pedido;

import java.util.List;

public interface IPedidoService {

    //Cantidad de ventas por valor y cantidad.
    List<ProductoVentaDTO> ventasRealizadas();

    //Obtener Todos los pedidos.
    List<Pedido> obtenerPedidos();

    //Obten Pedido por ID y Email. Lo usa el admin
    Pedido obtenerPedidoByIdEmail(Long id, String email);

    //Obtiene los pedidos hechos por un cliente a traves de su email (claim token)
    List<PedidosClienteDTO> obtenerPedidoByEmail();


    void crearPedido(PedidoCrearReqDTO pedido);

    void adjuntarComprobante(ComprobanteReqDTO comprobante);

    void cancelarPedido(Long id);

}
