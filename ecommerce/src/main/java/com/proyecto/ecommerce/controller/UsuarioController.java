package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidoCrearReqDTO;
import com.proyecto.ecommerce.service.PedidoService;
import com.proyecto.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

    //ModificarDatos

    //Solicitar Pedidos del Usuario

    @PostMapping("/pedido/crear")
    public ResponseEntity crearPedido (@Valid @RequestBody PedidoCrearReqDTO pedidoDTO)
    {
        pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.ok("Se creo el pedido con exito.");
    }

}
