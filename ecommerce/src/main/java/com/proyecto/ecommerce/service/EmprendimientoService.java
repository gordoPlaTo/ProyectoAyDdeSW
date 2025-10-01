package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.InfoEmpDTO;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IEmpRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmprendimientoService {
    @Autowired
    private IEmpRepository empRepository;

    public Optional<InfoEmpDTO> obtenerInfo(){
        return empRepository.findById(1L)
                .map(emp -> new InfoEmpDTO(
                        emp.getTitulo(),
                        emp.getDescripcion(),
                        emp.getDireccion(),
                        emp.getListContacto()
                ));
    }


}
