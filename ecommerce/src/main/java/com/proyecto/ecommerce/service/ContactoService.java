package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.ContactoRequestDTO;
import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.model.Contacto;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IContactoRepository;
import com.proyecto.ecommerce.repository.IEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoService implements IContactoService {
    @Autowired
    private IContactoRepository contactoRepository;
    @Autowired
    private IEmpRepository empRepository;

    @Override
    public List<Contacto> findAll() {
        return contactoRepository.findAll();
    }

    @Override
    public RespDTO deleteContacto(Long id) {
        if(contactoRepository.existsById(id)){
            contactoRepository.deleteById(id);
            return new RespDTO("Se elimino correctamente", LocalDateTime.now());
        }
        return new RespDTO("No se encontro el contacto que deseas eliminar",LocalDateTime.now());
    }

    @Override
    public Contacto save(ContactoRequestDTO contacto) {
        Contacto con = new Contacto(contacto.contacto());
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprendimiento no encontrado"));
        emp.getListContacto().add(con);
        return con;
    }
}
