package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.dto.EmprendimientoDTO.ContactoRequestDTO;
import com.proyecto.ecommerce.dto.EmprendimientoDTO.InfoEmpRequestDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialReqDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialesPatchDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoPatchDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoReqDTO;
import com.proyecto.ecommerce.dto.ProductosDTO.ProductoRespDTO;
import com.proyecto.ecommerce.model.IVA;
import com.proyecto.ecommerce.model.Material;
import com.proyecto.ecommerce.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @Autowired
    private  IvaService ivaService;


    @GetMapping("/iva/obtener")
    @PreAuthorize("hasRole('CLIENTE') || hasRole('ADMIN')")
    public ResponseEntity<List<IVA>> obtenerIva(){
        return ResponseEntity.ok(ivaService.obtenerIva());
    }

    @PatchMapping("/emp/info/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> modInfoEmprendimiento(@Valid @RequestBody InfoEmpRequestDTO infoReq){
        emprendimientoService.modInfoEmprendimiento(infoReq);
        return ResponseEntity.ok("Se completo correctamente la Modificacion de los datos de la empresa.");
    }
    //-----------------------------------Contactos--------------------------------------
    @PostMapping("/contacto/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearContacto(@RequestBody @Valid ContactoRequestDTO contacto){
        contactoService.save(contacto);
        return new ResponseEntity<>("Se creo el contacto con exito.", HttpStatus.OK);

    }

    @DeleteMapping("/contacto/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> EliminarContacto (@NotNull(message = "El id del contacto es obligatorio")
                                                         @Positive(message = "El id debe ser un número positivo")
                                                         @PathVariable Long id){
        return new ResponseEntity<>(contactoService.deleteContacto(id),HttpStatus.OK);
    }



    //-----------------------------------Productos--------------------------------------

    @PostMapping(value = "/producto/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearProducto (@Valid @ModelAttribute ProductoReqDTO prod){
        productoService.crearProducto(prod);
        return ResponseEntity.ok("Se completo la carga del producto correctamente.");
    }

    @PatchMapping("/producto/reducir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reducirStockProd(@NotNull(message = "El id del producto es obligatorio")
                                                       @Positive(message = "El id debe ser un número positivo")
                                                       @PathVariable Long id,
                                           @RequestParam int stock){
        productoService.reducirStock(id,stock);
        return ResponseEntity.ok("Se redujo el stock en " + stock + " unidades correctamente.");
    }
    @PatchMapping("/producto/aumentar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> aumentarStockProd(@NotNull(message = "El id del producto es obligatorio")
                                                        @Positive(message = "El id debe ser un número positivo") @PathVariable Long id,
                                           @RequestParam int stock){
        productoService.aumentarStock(id,stock);
        return ResponseEntity.ok("Se aumento el stock en " + stock + " unidades correctamente.");
    }

    @PatchMapping("/producto/estado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> habilitarDeshabilitarProducto (@NotNull(message = "El id del producto es obligatorio")
                                                                     @Positive(message = "El id debe ser un número positivo") @PathVariable Long id){
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
    public ResponseEntity<String> modificarProd(@PathVariable Long id,
                                        @Valid @RequestBody ProductoPatchDTO prodDTO){
        productoService.modificarProducto(id,prodDTO);
        return ResponseEntity.ok("Se modifico correctamente el producto seleccionado.");
    }
//-----------------------------------Materiales--------------------------------------
    @PostMapping("/material/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Material> crearMaterial (@Valid @ModelAttribute MaterialReqDTO matDTO){
        return ResponseEntity.ok(materialService.crearMaterial(matDTO));
    }

    @PatchMapping("/material/reducir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reducirStockMat(@PathVariable Long id,
                                           @RequestParam int stock){
        materialService.reducirStock(id,stock);
        return ResponseEntity.ok("Se redujo el stock en " + stock + " unidades correctamente.");
    }
    @PatchMapping("/material/aumentar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> aumentarStockMat(@PathVariable Long id,
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
    public ResponseEntity<String> modificarMat(@PathVariable Long id,
                                        @Valid @RequestBody MaterialesPatchDTO matDTO){
        materialService.modificarMaterial(id,matDTO);
        return ResponseEntity.ok("Se modifico correctamente el material seleccionado.");
    }

    @DeleteMapping("/material/borrar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> borrarMaterial (@PathVariable Long id){
        materialService.borrarMaterial(id);
        return ResponseEntity.ok("Se borro el material con exito.");
    }
}
