package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidosDTO.ComprobanteReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidosClienteDTO;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity crearPedido(@Valid @RequestBody PedidoCrearReqDTO pedidoDTO){
        pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok("Se cargo el pedido correctamente.");
    }

    //CancelarCompra
    @PatchMapping("/pedido/cliente/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity cancelarPedido (@Valid Long idPedido){
        pedidoService.cancelarPedido(idPedido);
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
    public ResponseEntity cargarComprobante(@Valid @ModelAttribute ComprobanteReqDTO compDTO){
        pedidoService.adjuntarComprobante(compDTO);
        return ResponseEntity.ok("Se cargo el pedido correctamente.");
    }


}
