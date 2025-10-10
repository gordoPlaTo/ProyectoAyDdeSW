package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.repository.IEmpRepository;
import com.proyecto.ecommerce.service.ContactoService;
import com.proyecto.ecommerce.service.EmprendimientoService;
import com.proyecto.ecommerce.service.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private EmprendimientoService emprendimientoService;

    @Autowired
    private ProductoService productoService;

    @PutMapping("/emp/info/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity modInfoEmprendimiento(@Valid @RequestBody InfoEmpRequestDTO infoReq){
        emprendimientoService.modInfoEmprendimiento(infoReq);
        return ResponseEntity.ok("Se completo correctamente la Modificacion de los datos de la empresa.");
    }

    @PostMapping("/contacto/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity crearContacto(@RequestBody @Valid ContactoRequestDTO contacto){
        contactoService.save(contacto);
        return new ResponseEntity<>("Se creo el contacto con exito", HttpStatus.OK);

    }

    @DeleteMapping("/contacto/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> EliminarContacto (@PathVariable
                                                         Long id){
        return new ResponseEntity<>(contactoService.deleteContacto(id),HttpStatus.OK);
    }

    //Endpoinsts para:

     //Acceder a estadisticas (Ventas, Producto mas comprado)
     //Obtener Listado de Pedidos Completados
     //Obtener Listado de Pedidos en Proceso

    @PostMapping("/producto/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto (@Valid @RequestBody ProductoReqDTO prod){
        return ResponseEntity.ok(productoService.crearProducto(prod));
    }
    @PostMapping("/producto/reducir/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity reducirStockProd(@PathVariable Long id,
                                           @RequestParam int stock){
        productoService.reducirStock(id,stock);
        return ResponseEntity.ok("Se redujo el stock en " + stock + " unidades correctamente.");
    }
    @PostMapping("/producto/aumentar/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity aumentarStockProd(@PathVariable Long id,
                                           @RequestParam int stock){
        productoService.aumentarStock(id,stock);
        return ResponseEntity.ok("Se aumento el stock en " + stock + " unidades correctamente.");
    }

    @PutMapping("/producto/estado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity habilitarDeshabilitarProducto (@PathVariable Long id){
        productoService.habDesProducto(id);
        return ResponseEntity.ok("Se cambio el estado del producto correctamente");
    }



    @PostMapping("/productos/obtenerTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Producto>> obtenerProductos(){
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

}
