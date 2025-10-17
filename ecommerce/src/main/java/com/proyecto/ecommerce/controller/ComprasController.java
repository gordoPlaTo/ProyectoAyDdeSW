package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    //GenerarCompra
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/pedido/crear")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity crearPedido(@Valid @RequestBody PedidoCrearReqDTO pedidoDTO){
        pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok("Se cargo el pedido correctamente.");
    }

    //CancelarCompra


    //Solicitar Pedidos del usuario


}
