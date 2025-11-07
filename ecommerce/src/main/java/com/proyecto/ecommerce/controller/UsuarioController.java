package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.UsuariosDTO.UserModDirecDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UserModPassDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UsuarioDatosDTO;
import com.proyecto.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/obtenerDatos")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<UsuarioDatosDTO> obtenerDatosPersonales(){
        return ResponseEntity.ok(usuarioService.obtenerDatosPersonales());
    }

    @PatchMapping("/modificar/password")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<String> modificarPassword(@Valid @RequestBody UserModPassDTO password){
        usuarioService.modificarPassword(password);
        return ResponseEntity.ok("Se cambio la contraseña del usuario exitosamente.");
    }

    @PatchMapping("/modificar/direccion")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<String> modificarDireccion(@Valid @RequestBody UserModDirecDTO direcDTO){
        usuarioService.modificarDireccion(direcDTO);
        return ResponseEntity.ok("Se cambio la direccion del usuario exitosamente.");
    }

    //Cambiar Nombre


    //Cambiar DNI



    @PatchMapping("/modificar/deshabilitarCuenta")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<String> deshabilitarCuenta(){
        usuarioService.deshabilitarCuenta();
        return ResponseEntity.ok("Se deshabilito la cuenta correctamente.");
    }


}
