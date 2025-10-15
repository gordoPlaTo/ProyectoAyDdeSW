package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.InfoEmpRequestDTO;
import com.proyecto.ecommerce.dto.InfoEmpResponseDTO;
import com.proyecto.ecommerce.model.Emprendimiento;
import com.proyecto.ecommerce.repository.IEmpRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EmprendimientoService implements IEmprendimientoService{
    @Autowired
    private IEmpRepository empRepository;

    @Override
    public boolean ValModAcces() {
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el emprendimiento."));
        return emp.isMod();
    }

    @Override
    public void modInfoEmprendimiento(InfoEmpRequestDTO InfoReq) {
        Emprendimiento emp = empRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el emprendimiento."));
        boolean fueModificado = false;


        if(InfoReq.titulo()!=null && !InfoReq.titulo().isBlank()){
            emp.setTitulo(InfoReq.titulo());
            fueModificado = true;
        }
        if(InfoReq.descripcion()!=null && !InfoReq.descripcion().isBlank()){
            emp.setDescripcion(InfoReq.descripcion());

        }
        if(InfoReq.direccion()!=null && !InfoReq.direccion().isBlank()){
            emp.setDireccion(InfoReq.direccion());
        }
        emp.setMod(true);
        empRepository.save(emp);
    }


    public InfoEmpResponseDTO obtenerInfo(){
        return empRepository.findById(1L)
                .map(emp -> new InfoEmpResponseDTO(
                        emp.getTitulo(),
                        emp.getDescripcion(),
                        emp.getDireccion(),
                        emp.getListContacto()
                )).orElseThrow(()-> new EntityNotFoundException("Algo ah sucedido mal, y no se encontro el emprendimiento."));
    }


}
