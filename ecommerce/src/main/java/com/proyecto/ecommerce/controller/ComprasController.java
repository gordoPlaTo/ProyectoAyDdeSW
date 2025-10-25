package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidosDTO.ComprobanteReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCompletarDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidosClienteDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    //GenerarCompra
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/pedido/cliente/crear")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> crearPedido(@Valid @RequestBody PedidoCrearReqDTO pedidoDTO){
        pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok("Se cargo el pedido correctamente.");
    }

    //CancelarCompra
    @PatchMapping("/pedido/cliente/cancelar/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> cancelarPedido (@NotNull(message = "El id del pedido es obligatorio")
                                                      @Positive(message = "El id debe ser un número positivo")
                                                      @PathVariable Long id){
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok("Se elimino correctamente el pedido ");
    }

    //Solicitar Pedidos del usuario
    @GetMapping("pedido/cliente/obtener")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PedidosClienteDTO>> obtenerPedidosCliente(){
        return ResponseEntity.ok(pedidoService.obtenerPedidoByEmail());
    }

    //Subir Comprobante (cliente)
    @PatchMapping("/pedido/comprobante/cargar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> cargarComprobante(@Valid @ModelAttribute ComprobanteReqDTO compDTO){
        pedidoService.adjuntarComprobante(compDTO);
        return ResponseEntity.ok("Se cargo el pedido correctamente.");
    }

    //Marcar como completo el pedido
    @PatchMapping("/completarPedido")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> completarPedido(@Valid @RequestBody PedidoCompletarDTO completarDTO){
        pedidoService.completarPedido(completarDTO);

        return ResponseEntity.ok("Se completo el pedido con exito.");
    }

    //Endpoinsts para:

    //Acceder a estadisticas (Ventas, Producto mas comprado)
    //Obtener Listado de Pedidos Completados

    @GetMapping("/pedido/ventasRealizadas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductoVentaDTO> obtenerVentas(){
        return pedidoService.ventasRealizadas();
    }

    //Obtener Listado de Pedidos
    @GetMapping("/pedido/obtenerPedidos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Pedido> obtenerPedidos(){
        return pedidoService.obtenerPedidos();
    }

}
