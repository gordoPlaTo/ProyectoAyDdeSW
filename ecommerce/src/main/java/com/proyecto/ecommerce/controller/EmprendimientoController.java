package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.EmprendimientoDTO.InfoEmpResponseDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoActRespDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoRespDTO;
import com.proyecto.ecommerce.model.Contacto;
import com.proyecto.ecommerce.service.ContactoService;
import com.proyecto.ecommerce.service.EmprendimientoService;
import com.proyecto.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/emprendimiento")
public class EmprendimientoController{
    //Endpoints para Obtener Info Emprendimiento, Solicitar todos los Productos

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/contactos")
    public ResponseEntity<List<Contacto>> obtenerContactos(){
        List<Contacto> contactos = contactoService.findAll();
            if (contactos.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encontro ningun contacto almacenado");
            }
        return ResponseEntity.ok(contactos);
    }

    @Autowired
    private EmprendimientoService emprendimientoService;

    @GetMapping("/obtener")
    public ResponseEntity<InfoEmpResponseDTO> obtenerInfoEmprendimiento(){
        InfoEmpResponseDTO info = emprendimientoService.obtenerInfo();
        return ResponseEntity.ok(info);
    }

    //Método para validar que el cliente principal, cambie datos antes de seguir
    //con cualquier otra funcionalidad del sistema
    @GetMapping("/emp/valModAcces")
    public ResponseEntity<Boolean> datosEstanCargados(){
        return ResponseEntity.ok(emprendimientoService.ValModAcces());
    }

    @GetMapping("/productos/obtenerActivos")
    public ResponseEntity<List<ProductoActRespDTO>> obtenerProductosActivos(){
        return ResponseEntity.ok(productoService.obtenerProductosActivos());
    }
}
