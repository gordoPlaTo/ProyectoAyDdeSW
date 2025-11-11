package com.proyecto.ecommerce.service;


import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialReqDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialesPatchDTO;
import com.proyecto.ecommerce.model.Material;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMaterialService {
    Material crearMaterial(MaterialReqDTO matDTO);

    Material obtenerMaterialById (Long id);
    List<Material> obtenerMateriales();

    void modificarMaterial(Long id, MaterialesPatchDTO matDTO);

    //Reducir Stock
    void reducirStock(Long id, int stock);

    //Aumentar Stock
    void aumentarStock(Long id, int stock);

    Material borrarMaterial(Long id);

    void modificarImagen(Long id, MultipartFile img);
}
