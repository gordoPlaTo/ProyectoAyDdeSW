package com.proyecto.ecommerce.dto.EmprendimientoDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.proyecto.ecommerce.model.Contacto;

import java.util.List;

@JsonPropertyOrder
public record InfoEmpResponseDTO(
        String titulo,
        String descripcion,
        String direccion,
        String cuit,
        String email,
        List<Contacto> contactos
) {
}
