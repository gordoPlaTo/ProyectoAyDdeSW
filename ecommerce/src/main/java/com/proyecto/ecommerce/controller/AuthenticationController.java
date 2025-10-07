package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.AuthLoginRequestDTO;
import com.proyecto.ecommerce.dto.AuthResponseDTO;
import com.proyecto.ecommerce.dto.RegisterRequestDTO;
import com.proyecto.ecommerce.dto.RegisterResponseDTO;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.model.Permission;
import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.repository.IEmpRepository;
import com.proyecto.ecommerce.repository.IPermissionRepository;
import com.proyecto.ecommerce.repository.IRoleRepository;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IPermissionRepository permissionRepository;
    @Autowired
    private IEmpRepository empRepository;

    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@Valid @RequestBody AuthLoginRequestDTO userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUserEmail(userRequest), HttpStatus.OK);
    }

    //Como tenemos que validar que el dueño del sistema sea el que primero se
    //loguee y modifique la informacion del emprendimiento (minimo de personalizacion)
    //en caso de que no este registrado ningun Emprendimiento mostrará un mensaje
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerClient (@Valid @RequestBody RegisterRequestDTO register){
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("El emprendimiento principal no se definio eb la base de datos"));


        if(!emp.isMod()){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RegisterResponseDTO("Para acceder a esta funcionalidad, el cliente principal de este sistema" +
                            " debe actualizar la informacion del emprendimiento negocio.",false));
        }
        return  new ResponseEntity<>(this.userDetailsService.register(register, 1L),HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisterResponseDTO> registerAdmin (@Valid @RequestBody RegisterRequestDTO registerRequest){
        System.out.println("DTO recibido: " + registerRequest);

        return  new ResponseEntity<>(this.userDetailsService.register(registerRequest, 2L), HttpStatus.OK);
    }

}
