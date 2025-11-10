package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.EmprendimientoDTO.ContactoRequestDTO;
import com.proyecto.ecommerce.dto.RespDTO;
import com.proyecto.ecommerce.model.Contacto;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IContactoRepository;
import com.proyecto.ecommerce.repository.IEmpRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

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
        if(!contactoRepository.existsById(id)){
            throw new EntityNotFoundException("No se encontro el contacto que deseas eliminar");
        }
        contactoRepository.deleteById(id);
        return new RespDTO("Se elimino correctamente",true, LocalDateTime.now());
    }

    @Override
    public Contacto save(ContactoRequestDTO contacto) {
        Contacto con = new Contacto(contacto.contacto());
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprendimiento no encontrado"));
        con.setEmprendimiento(emp);
        contactoRepository.save(con);
        return con;
    }
}
