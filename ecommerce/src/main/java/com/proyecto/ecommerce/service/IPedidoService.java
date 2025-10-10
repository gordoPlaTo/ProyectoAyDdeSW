package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductoMasVendidoDTO;
import com.proyecto.ecommerce.dto.ProductoRespDTO;
import com.proyecto.ecommerce.dto.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.model.Producto;

import java.util.List;

public interface IPedidoService {

    //Producto mas vendido
    // ProductoMasVendidoDTO prodMasVendido();

    //Cantidad de ventas por valor y cantidad.
    //ProductoVentaDTO ventasRealizadas();

    //Obtener Todos los pedidos.
    List<Pedido> obtenerPedidos();

    //Obten Pedido por ID.
    Pedido findPedidoById(Long id);

}
