package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.dto.authDTO.AuthLoginRequestDTO;
import com.proyecto.ecommerce.dto.authDTO.AuthResponseDTO;
import com.proyecto.ecommerce.dto.authDTO.RegisterRequestDTO;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IEmpRepository;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {
    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IEmpRepository empRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@Valid @RequestBody AuthLoginRequestDTO userRequest){
        return ResponseEntity.ok(this.userDetailsService.loginUserEmail(userRequest));
    }

    //Como tenemos que validar que el dueño del sistema sea el que primero se
    //loguee y modifique la informacion del emprendimiento (minimo de personalizacion)
    //en caso de que no este registrado ningun Emprendimiento mostrará un mensaje
    @PostMapping("/register")
    public ResponseEntity<RespDTO> registerClient (@Valid @RequestBody RegisterRequestDTO register){
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("El emprendimiento principal no se definio en la base de datos"));

        if(!emp.isMod()){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RespDTO("Para acceder a esta funcionalidad, el cliente principal" +
                            " debe actualizar la informacion del emprendimiento.",false, LocalDateTime.now()));
        }
        return ResponseEntity.ok(this.userDetailsService.register(register, 1L));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<RespDTO> registerAdmin (@Valid @RequestBody RegisterRequestDTO registerRequest){
        return ResponseEntity.ok(this.userDetailsService.register(registerRequest, 2L));
    }

}
