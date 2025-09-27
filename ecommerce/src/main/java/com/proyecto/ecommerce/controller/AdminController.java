package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.RegisterRequestDTO;
import com.proyecto.ecommerce.dto.RegisterResponseDTO;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("DenyAll()")
public class AdminController {
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register (@RequestBody @Valid RegisterRequestDTO registerRequest){
        return  new ResponseEntity<>(this.userDetailsService.register(registerRequest, 2L), HttpStatus.OK);
    }




    //Endpoinsts para:
     //Cambiar la descripcion del negocio
     //Acceder a estadisticas (Ventas, Producto mas comprado)
     //Obtener Listado de Pedidos Completados
     //Obtener Listado de Pedidos en Proceso




}
