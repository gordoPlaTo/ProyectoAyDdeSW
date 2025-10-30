package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.EmprendimientoDTO.ContactoRequestDTO;
import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.model.Contacto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IContactoService {
    List<Contacto> findAll();
    RespDTO deleteContacto(Long id);
    Contacto save(ContactoRequestDTO contacto);


}
