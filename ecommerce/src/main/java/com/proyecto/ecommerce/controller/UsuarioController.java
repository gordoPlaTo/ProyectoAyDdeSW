package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
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



}
