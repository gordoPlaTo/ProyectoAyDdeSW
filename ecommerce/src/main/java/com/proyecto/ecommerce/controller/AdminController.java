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

import java.time.LocalDateTime;
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

    @PatchMapping("/emprendimiento/info/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> modInfoEmprendimiento(@Valid @RequestBody InfoEmpRequestDTO infoReq){
        emprendimientoService.modInfoEmprendimiento(infoReq);
        RespDTO response = new RespDTO(
                "Se completó correctamente la modificación de los datos de la empresa.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);    }
    //-----------------------------------Contactos--------------------------------------
    @PostMapping("/contacto/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> crearContacto(@RequestBody @Valid ContactoRequestDTO contacto){
        contactoService.save(contacto);
        RespDTO response = new RespDTO(
                "Se creó el contacto con éxito.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
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
    public ResponseEntity<RespDTO> crearProducto (@Valid @ModelAttribute ProductoReqDTO prod){
        if (prod.imgProducto().getSize() > 5_000_000){//asi se indica el tamaño aca 5mb maximo
            throw new IllegalArgumentException("El tamaño de la imagen excede las 5mb permitidos");
        }

        String tipoArchivo = prod.imgProducto().getContentType();
        if (tipoArchivo == null || !tipoArchivo.startsWith("image/")){
            //aca validamos el tipo de archivo para que sea una imagen
            throw  new IllegalArgumentException("El archivo ingresado debe ser una imagen valida");
        }

        productoService.crearProducto(prod);

        RespDTO response = new RespDTO(
                "Se completó la carga del producto correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    }

    @PatchMapping("/producto/reducir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> reducirStockProd(@NotNull(message = "El id del producto es obligatorio")
                                                       @Positive(message = "El id debe ser un número positivo")
                                                       @PathVariable Long id,
                                           @RequestParam int stock){
        productoService.reducirStock(id,stock);

        RespDTO response = new RespDTO(
                "Se redujo el stock en " + stock + " unidades correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);    }


    @PatchMapping("/producto/aumentar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> aumentarStockProd(@NotNull(message = "El id del producto es obligatorio")
                                                        @Positive(message = "El id debe ser un número positivo") @PathVariable Long id,
                                           @RequestParam int stock){
        productoService.aumentarStock(id,stock);

        RespDTO response = new RespDTO(
                "Se aumentó el stock en " + stock + " unidades correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/producto/estado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> habilitarDeshabilitarProducto (@NotNull(message = "El id del producto es obligatorio")
                                                                     @Positive(message = "El id debe ser un número positivo") @PathVariable Long id){
        productoService.habDesProducto(id);

        RespDTO response = new RespDTO(
                "Se cambió el estado del producto correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);

    }

    @GetMapping("/producto/obtenerTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoRespDTO>> obtenerProductos(){
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

    @PatchMapping("/producto/mod/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> modificarProd(@PathVariable Long id,
                                        @Valid @RequestBody ProductoPatchDTO prodDTO){
        productoService.modificarProducto(id,prodDTO);
        RespDTO response = new RespDTO(
                "Se modificó correctamente el producto seleccionado.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
//-----------------------------------Materiales--------------------------------------
    @PostMapping("/material/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Material> crearMaterial (@Valid @ModelAttribute MaterialReqDTO matDTO){

        if (matDTO.imgMaterial().getSize() > 5_000_000){//asi se indica el tamaño aca 5mb maximo
            throw new IllegalArgumentException("El tamaño de la imagen excede las 5mb permitidos");
        }
        String tipoArchivo = matDTO.imgMaterial().getContentType();
        if (tipoArchivo == null || !tipoArchivo.startsWith("image/")){
            //aca validamos el tipo de archivo para que sea una imagen
            throw  new IllegalArgumentException("El archivo ingresado debe ser una imagen valida");
        }

        return ResponseEntity.ok(materialService.crearMaterial(matDTO));
    }

    @PatchMapping("/material/reducir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> reducirStockMat(@PathVariable Long id,
                                           @RequestParam int stock){
        materialService.reducirStock(id,stock);

        RespDTO response = new RespDTO(
                "Se redujo el stock en " + stock + " unidades correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/material/aumentar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> aumentarStockMat(@PathVariable Long id,
                                            @RequestParam int stock){
        materialService.aumentarStock(id,stock);

        RespDTO response = new RespDTO(
                "Se aumentó el stock en " + stock + " unidades correctamente.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/material/obtenerTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Material>> obtenerMateriales(){
        return ResponseEntity.ok(materialService.obtenerMateriales());
    }

    @PatchMapping("/material/mod/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> modificarMat(@PathVariable Long id,
                                        @Valid @RequestBody MaterialesPatchDTO matDTO){
        materialService.modificarMaterial(id,matDTO);

        RespDTO response = new RespDTO(
                "Se modificó correctamente el material seleccionado.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);    }

    @DeleteMapping("/material/borrar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RespDTO> borrarMaterial (@PathVariable Long id){
        materialService.borrarMaterial(id);

        RespDTO response = new RespDTO(
                "Se borró el material con éxito.",
                true,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);    }
}
