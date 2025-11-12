package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UserModDirecDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UserModPassDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UsuarioDatosDTO;
import com.proyecto.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
    public ResponseEntity<RespDTO> modificarPassword(@Valid @RequestBody UserModPassDTO password){
        usuarioService.modificarPassword(password);
        RespDTO response = new RespDTO(
                "Se cambió la contraseña del usuario exitosamente.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PatchMapping("/modificar/direccion")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<RespDTO> modificarDireccion(@Valid @RequestBody UserModDirecDTO direcDTO){
        usuarioService.modificarDireccion(direcDTO);
        RespDTO response = new RespDTO(
                "Se cambió la dirección del usuario exitosamente.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PatchMapping("/modificar/deshabilitarCuenta")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<RespDTO> deshabilitarCuenta(){
        usuarioService.deshabilitarCuenta();
        RespDTO response = new RespDTO(
                "Se deshabilitó la cuenta correctamente.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PatchMapping("/modificar/imgPerfil")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<RespDTO> modificarFotoPerfil(@NotNull(message = "Debes incluir una imagen para asignarla al perfil")
                                                       @RequestParam MultipartFile imagenPerfil){

        if (imagenPerfil.getSize() > 5_000_000){//asi se indica el tamaño aca 5mb maximo
            throw new IllegalArgumentException("El tamaño de la imagen excede las 5mb permitidos");
        }
        String tipoArchivo = imagenPerfil.getContentType();
        if (tipoArchivo == null || !tipoArchivo.startsWith("image/")){
            //aca validamos el tipo de archivo para que sea una imagen
            throw  new IllegalArgumentException("El archivo ingresado debe ser una imagen valida");
        }

        usuarioService.modificarImagenPefil(imagenPerfil);


        RespDTO response = new RespDTO(
                "Se modifico la imagen de perfil correctamente.",
                true,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

}
