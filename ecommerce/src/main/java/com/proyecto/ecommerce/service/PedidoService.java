package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ProductoMasVendidoDTO;
import com.proyecto.ecommerce.dto.ProductoRespDTO;
import com.proyecto.ecommerce.dto.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.repository.IPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService implements IPedidoService {
    @Autowired
    private IPedidoRepository pedidoRepository;
/*
    @Override
    public ProductoMasVendidoDTO prodMasVendido() {
       return pedidoRepository.productoMasVendido();
    }*/

  /*  @Override
    public ProductoVentaDTO ventasRealizadas() {
        Object[] consulta =  pedidoRepository.ventasPedido();
        BigDecimal total = (BigDecimal) consulta[0];
        int cantidad = (int) consulta[1];

        return new ProductoVentaDTO(total, cantidad);
    }
*/
    @Override
    public List<Pedido> obtenerPedidos() {
        return List.of();
    }

    @Override
    public Pedido findPedidoById(Long id) {
        return null;
    }
}
