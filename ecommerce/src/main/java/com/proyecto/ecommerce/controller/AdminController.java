package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.model.Contacto;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IContactoRepository;
import com.proyecto.ecommerce.repository.IEmpRepository;
import com.proyecto.ecommerce.service.ContactoService;
import com.proyecto.ecommerce.service.EmprendimientoService;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register (@RequestBody @Valid RegisterRequestDTO registerRequest){
        return  new ResponseEntity<>(this.userDetailsService.register(registerRequest, 2L), HttpStatus.OK);
    }


    @Autowired
    private ContactoService contactoService;

    @Autowired
    private IEmpRepository empRepository;

    @Autowired
    private EmprendimientoService emprendimientoService;

    @GetMapping("/emp/info")
    public ResponseEntity<InfoEmpDTO> obtenerInfoEmprendimiento(){
        return emprendimientoService.obtenerInfo()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/emp/info/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity modInfoEmprendimiento(@RequestBody @Valid InfoEmpDTO InfoReq){
        Emprendimiento emp = empRepository.findById(1L).orElse(null);
        if (emp == null){
            return new ResponseEntity<>("No hay ningun emprendimiento creado",HttpStatus.BAD_REQUEST);
        }

        if(InfoReq.titulo()!=null & !InfoReq.titulo().isBlank()){
            emp.setTitulo(InfoReq.titulo());
        }
        if(InfoReq.descripcion()!=null & !InfoReq.descripcion().isBlank()){
            emp.setDescripcion(InfoReq.descripcion());
        }
        if(InfoReq.direccion()!=null & !InfoReq.direccion().isBlank()){
            emp.setDireccion(InfoReq.direccion());
        }
        empRepository.save(emp);
        return new ResponseEntity<>("Se completó la modificación de datos del Emprendimiento",HttpStatus.OK);
    }

    @PostMapping("/emp/info/contacto/new")
    public ResponseEntity crearContacto(@RequestBody @Valid ContactoRequestDTO contacto){
        contactoService.save(contacto);
        return new ResponseEntity<>("Se creo el contacto con exito", HttpStatus.OK);

    }

    @GetMapping("/emp/info/contacto")
    public ResponseEntity<List<Contacto>> obtenerContactos(){
        List<Contacto> contactos = contactoService.findAll();

        if (contactos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(contactos, HttpStatus.OK);
    }

    @DeleteMapping("/borrar")
    public ResponseEntity<RespDTO> EliminarContacto (@PathVariable Long id){
        return new ResponseEntity<>(contactoService.deleteContacto(id),HttpStatus.OK);
    }

    //Endpoinsts para:
     //Cambiar la descripcion del negocio
     //Acceder a estadisticas (Ventas, Producto mas comprado)
     //Obtener Listado de Pedidos Completados
     //Obtener Listado de Pedidos en Proceso




}
