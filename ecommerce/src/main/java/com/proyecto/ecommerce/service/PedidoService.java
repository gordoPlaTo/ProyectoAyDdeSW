package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.PedidosDTO.*;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.model.*;
import com.proyecto.ecommerce.repository.IEstadoPedidoRepository;
import com.proyecto.ecommerce.repository.IPedidoRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private IEstadoPedidoRepository estadoPedido;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<ProductoVentaDTO> ventasRealizadas() {
        return pedidoRepository.ventasPedido();
    }

    @Override //obtenemos los pedidos del cliente
    public List<PedidosClienteDTO> obtenerPedidoByEmail() {
        //Obtenemos el subject del token almacenado en el security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return pedidoRepository.obtenerPedidosPorEmail(email).stream()
                .map(ped -> new PedidosClienteDTO(
                        ped.getIdPedido(),
                        ped.getFechaCreacion(),
                        ped.getTotalCompra(),
                        ped.getUrlComprobante(),
                        ped.getListDetallePedido().stream()
                                .map(det -> new DetallePedidoRespDTO(
                                        det.getIdDetalle(),
                                        det.getCantidad(),
                                        det.getPrecioNeto(),
                                        det.getMontoIva(),
                                        det.getPrecioTotal(),
                                        det.getProducto().getNombre()
                                ))
                                .toList(),
                        ped.getUsuario().getUsername(),
                        ped.getUsuario().getApellido(),
                        ped.getUsuario().getEmail(),
                        ped.getEstadoPedido().getDescripcion()
                ))
                .toList();
    }


    @Override
    public void crearPedido(PedidoCrearReqDTO pedido) {
        Pedido ped = new Pedido();
        ped.setFechaCreacion(LocalDate.now());
        //Validar si existe el producto


        List<DetallePedido> listDetalle = pedido.listProductos().stream()
                        .map(det ->{
                            Producto producto = productoService.obtenerProductoById(det.id());
                            if(!productoService.elProductoExiste(det.id())){
                                throw new NoSuchElementException("Uno de los producto que intentas comprar no existe. ID buscado: " + det.id() );
                            }

                            productoService.reducirStock(det.id(), det.cantidad());

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


        Usuario usu = usuarioService.obtenerUsuarioByEmail();
        ped.setUsuario(usu);

        ped.setEstadoPedido(estadoPedido.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado de pedido con id 1")));

        pedidoRepository.save(ped);
    }

    @Override
    public void adjuntarComprobante(ComprobanteReqDTO comprobante) {
        Pedido ped = this.obtenerPedidoByIdEmail(comprobante.idPedido(),
                SecurityContextHolder.getContext().getAuthentication().getName());

        ped.setUrlComprobante(cloudinaryService.subirImagen(comprobante.comprobante(),"comprobante"));

        pedidoRepository.save(ped);
    }

    @Override
    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPedidoByIdEmail(Long id,String email) { //Lo usa el admin para buscar el pedido del cliente
        return pedidoRepository.obtenerPedidoPorIdYEmail(id,email)
                .orElseThrow(()-> new EntityNotFoundException("No se encontro el pedido que estas buscando, puede ser" +
                        " por un id o email incorrecto."));
    }

    @Override
    public void cancelarPedido(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        Pedido ped = this.obtenerPedidoByIdEmail(id,email);


        EstadoPedido estado = estadoPedido.findById(3L)
                .orElseThrow(()-> new EntityNotFoundException("No se encontro el estado a asignar al pedido."));

        ped.setEstadoPedido(estado);




        pedidoRepository.save(ped);
    }

    @Override
    public void completarPedido(PedidoCompletarDTO compDTO) {
        Pedido ped = this.obtenerPedidoByIdEmail(compDTO.id(),compDTO.email());

        if (ped.getUrlComprobante() == null || ped.getUrlComprobante().isBlank()){
            throw new IllegalArgumentException("No puedes completar un pedido al que no se le adjuntó su correspondiente comprobante de pago.");
        }

        EstadoPedido estado = estadoPedido.findById(2L)
                .orElseThrow(()-> new EntityNotFoundException("No se encontro el estado a asignar al pedido."));

        ped.setEstadoPedido(estado);

        pedidoRepository.save(ped);
    }
}
