package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.dto.EmprendimientoDTO.InfoEmpRequestDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialReqDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialesPatchDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidoCrearReqDTO;
import com.proyecto.ecommerce.dto.PedidosDTO.PedidosClienteDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoPatchDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoRespDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Material;
import com.proyecto.ecommerce.model.Pedido;
import com.proyecto.ecommerce.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private MaterialService materialService;

    @Autowired
    private PedidoService pedidoService;

    @PutMapping("/emp/info/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity modInfoEmprendimiento(@Valid @RequestBody InfoEmpRequestDTO infoReq){
        emprendimientoService.modInfoEmprendimiento(infoReq);
        return ResponseEntity.ok("Se completo correctamente la Modificacion de los datos de la empresa.");
    }
    //-----------------------------------Contactos--------------------------------------
    @PostMapping("/contacto/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity crearContacto(@RequestBody @Valid ContactoRequestDTO contacto){
        contactoService.save(contacto);
        return new ResponseEntity<>("Se creo el contacto con exito.", HttpStatus.OK);

    }

    @DeleteMapping("/contacto/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> EliminarContacto (@PathVariable
                                                         Long id){
        return new ResponseEntity<>(contactoService.deleteContacto(id),HttpStatus.OK);
    }
    //-----------------------------------Pedidos--------------------------------------

    //Endpoinsts para:
     //Acceder a estadisticas (Ventas, Producto mas comprado)
    @GetMapping("/pedido/ventasRealizadas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductoVentaDTO> obtenerVentas(){
        return pedidoService.ventasRealizadas();
    }
     //Obtener Listado de Pedidos Completados

     //Obtener Listado de Pedidos
     @GetMapping("/pedido/obtenerPedidos")
     @PreAuthorize("hasRole('ADMIN')")
     public List<Pedido> obtenerPedidos(){
         return pedidoService.obtenerPedidos();
     }


    //-----------------------------------Productos--------------------------------------

    @PostMapping(value = "/producto/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity crearProducto (@Valid @ModelAttribute ProductoReqDTO prod){
        productoService.crearProducto(prod);
        return ResponseEntity.ok("Se completo la carga del producto correctamente.");
    }
    @PatchMapping("/producto/reducir/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity reducirStockProd(@PathVariable Long id,
                                           @RequestParam int stock){
        productoService.reducirStock(id,stock);
        return ResponseEntity.ok("Se redujo el stock en " + stock + " unidades correctamente.");
    }
    @PatchMapping("/producto/aumentar/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity aumentarStockProd(@PathVariable Long id,
                                           @RequestParam int stock){
        productoService.aumentarStock(id,stock);
        return ResponseEntity.ok("Se aumento el stock en " + stock + " unidades correctamente.");
    }

    @PatchMapping("/producto/estado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity habilitarDeshabilitarProducto (@PathVariable Long id){
        productoService.habDesProducto(id);
        return ResponseEntity.ok("Se cambio el estado del producto correctamente.");
    }

    @GetMapping("/producto/obtenerTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoRespDTO>> obtenerProductos(){
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

    @PatchMapping("/producto/mod/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity modificarProd(@PathVariable Long id,
                                        @Valid @RequestBody ProductoPatchDTO prodDTO){
        productoService.modificarProducto(id,prodDTO);
        return ResponseEntity.ok("Se modifico correctamente el producto seleccionado.");
    }
//-----------------------------------Materiales--------------------------------------
@PostMapping("/material/crear")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Material> crearMaterial (@Valid @RequestBody MaterialReqDTO matDTO){
    return ResponseEntity.ok(materialService.crearMaterial(matDTO));
}
    @PatchMapping("/material/reducir/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity reducirStockMat(@PathVariable Long id,
                                           @RequestParam int stock){
        materialService.reducirStock(id,stock);
        return ResponseEntity.ok("Se redujo el stock en " + stock + " unidades correctamente.");
    }
    @PatchMapping("/material/aumentar/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity aumentarStockMat(@PathVariable Long id,
                                            @RequestParam int stock){
        materialService.aumentarStock(id,stock);
        return ResponseEntity.ok("Se aumento el stock en " + stock + " unidades correctamente.");
    }


    @GetMapping("/material/obtenerTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Material>> obtenerMateriales(){
        return ResponseEntity.ok(materialService.obtenerMateriales());
    }

    @PatchMapping("/material/mod/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity modificarMat(@PathVariable Long id,
                                        @Valid @RequestBody MaterialesPatchDTO matDTO){
        materialService.modificarMaterial(id,matDTO);
        return ResponseEntity.ok("Se modifico correctamente el material seleccionado.");
    }

}
