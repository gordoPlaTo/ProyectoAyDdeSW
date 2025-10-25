package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.EmprendimientoDTO.InfoEmpRequestDTO;
import com.proyecto.ecommerce.dto.EmprendimientoDTO.InfoEmpResponseDTO;

public interface IEmprendimientoService {

    InfoEmpResponseDTO obtenerInfo();

    void modInfoEmprendimiento(InfoEmpRequestDTO InfoReq);


     boolean ValModAcces();

}
