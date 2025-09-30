package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.InfoEmpDTO;
import com.proyecto.ecommerce.dto.RegisterRequestDTO;
import com.proyecto.ecommerce.dto.RegisterResponseDTO;
import com.proyecto.ecommerce.model.Contacto;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IContactoRepository;
import com.proyecto.ecommerce.repository.IEmpRepository;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("DenyAll()")
public class AdminController {
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register (@RequestBody @Valid RegisterRequestDTO registerRequest){
        return  new ResponseEntity<>(this.userDetailsService.register(registerRequest, 2L), HttpStatus.OK);
    }


    @Autowired
    private IContactoRepository contactoRepository;

    @Autowired
    private IEmpRepository empRepository;

    @PutMapping("/data/emp")
    public ResponseEntity modInfoEmprendimiento(@RequestBody @Valid InfoEmpDTO InfoReq){
        Emprendimiento emp = empRepository.findById(1L).orElse(null);
        if (emp == null){
            return new ResponseEntity<>("No hay ningun emprendimiento creado",HttpStatus.OK);
        }

        if(InfoReq.titulo()!=null || !InfoReq.contactos().isEmpty()){
            emp.setTitulo(InfoReq.titulo());
        }
        if(InfoReq.descripcion()!=null || !InfoReq.descripcion().isEmpty()){
            emp.setDireccion(InfoReq.descripcion());
        }
        if(!InfoReq.contactos().isEmpty()){
            for (Contacto con : InfoReq.contactos()){
                contactoRepository.save(con);
            }
            List<Contacto> listContacto = contactoRepository.findAll();
            emp.setListContacto(listContacto);
        }

        empRepository.save(emp);

        return new ResponseEntity<>("Se completó la modificación de datos del Emprendimiento",HttpStatus.OK);
    }

    //Endpoinsts para:
     //Cambiar la descripcion del negocio
     //Acceder a estadisticas (Ventas, Producto mas comprado)
     //Obtener Listado de Pedidos Completados
     //Obtener Listado de Pedidos en Proceso




}
