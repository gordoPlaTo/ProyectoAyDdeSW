package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.InfoEmpResponseDTO;
import com.proyecto.ecommerce.repository.IEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmprendimientoService {
    @Autowired
    private IEmpRepository empRepository;

    public Optional<InfoEmpResponseDTO> obtenerInfo(){
        return empRepository.findById(1L)
                .map(emp -> new InfoEmpResponseDTO(
                        emp.getTitulo(),
                        emp.getDescripcion(),
                        emp.getDireccion(),
                        emp.getListContacto()
                ));
    }


}
