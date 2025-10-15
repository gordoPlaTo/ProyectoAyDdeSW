package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.ProductoVentaDTO;
import com.proyecto.ecommerce.model.DetallePedido;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IEstadoPedido;
import com.proyecto.ecommerce.repository.IPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService implements IPedidoService {
    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IEstadoPedido estadoPedido;


    @Override
    public List<ProductoVentaDTO> ventasRealizadas() {
        return pedidoRepository.ventasPedido();
    }

    @Override //obtenemos los pedidos del cliente
    public List<Pedido> obtenerPedidoByEmail() { //Obtenemos el subject del token almacenado en el security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return pedidoRepository.obtenerPedidosPorEmail(email);
    }

    @Override
    public void crearPedido(PedidoCrearReqDTO pedido) {
        Pedido ped = new Pedido();
        ped.setFechaCreacion(LocalDate.now());
        List<DetallePedido> listDetalle = pedido.listProductos().stream()
                        .map(det ->{
                            Producto producto = productoService.obtenerProductoById(det.id());
                            BigDecimal iva = producto.getIva().getPorcentaje();

                            BigDecimal precioNeto = producto.getPrecio()
                                    .multiply(BigDecimal.valueOf(det.cantidad()));

                            BigDecimal montoIva = precioNeto.multiply(iva.divide(BigDecimal.valueOf(100)));

                            BigDecimal precioTotal = precioNeto.add(montoIva);
                            return new DetallePedido(det.cantidad(),ped,precioNeto,montoIva,precioTotal,producto);
                        })
                                .toList();

        ped.setListDetallePedido(listDetalle);

        BigDecimal totalCompra = listDetalle.stream()
                        .map(DetallePedido::getPrecioTotal)
                                .reduce(BigDecimal.ZERO,BigDecimal::add);

        ped.setTotalCompra(totalCompra);

        Usuario usu = usuarioService.obtenerUsuarioByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        ped.setUsuario(usu);

        ped.setEstadoPedido(estadoPedido.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado de pedido con id 1")));

        pedidoRepository.save(ped);
    }


    @Override
    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPedidoByIdEmail(Long id,String email) { //Lo usa el admin para buscar el pedido del cliente
        return pedidoRepository.obtenerPedidoPorIdYEmail(id,email)
                .orElseThrow(()-> new NoSuchElementException("No se encontro el pedido que estas buscando, puede ser" +
                        " por un id o email incorrecto."));
    }

}
