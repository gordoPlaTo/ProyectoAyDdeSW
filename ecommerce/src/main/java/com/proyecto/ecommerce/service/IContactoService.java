package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ContactoRequestDTO;
import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.model.Contacto;

import java.util.List;

public interface IContactoService {
    List<Contacto> findAll();
    RespDTO deleteContacto(Long id);
    Contacto save(ContactoRequestDTO contacto);


}
