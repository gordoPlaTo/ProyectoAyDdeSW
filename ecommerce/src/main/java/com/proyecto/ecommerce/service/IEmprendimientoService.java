package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.InfoEmpRequestDTO;
import com.proyecto.ecommerce.dto.InfoEmpResponseDTO;
import com.proyecto.ecommerce.model.Pedido;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface IEmprendimientoService {
    InfoEmpResponseDTO obtenerInfo();

    void modInfoEmprendimiento(InfoEmpRequestDTO InfoReq);


     boolean ValModAcces();

}
