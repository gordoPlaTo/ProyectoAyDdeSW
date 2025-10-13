package com.proyecto.ecommerce.service;


import com.proyecto.ecommerce.dto.MaterialReqDTO;
import com.proyecto.ecommerce.model.Material;
import com.proyecto.ecommerce.model.Producto;

import java.util.List;

public interface IMaterialService {
    Material crearMaterial(MaterialReqDTO matDTO);

    Material obtenerMaterialById (Long id);
    List<Material> obtenerMateriales();

    //Modificar Material (nombre y descripcion)
    void modificarMaterial(Long id, MaterialReqDTO matDTO);

    //Reducir Stock
    void reducirStock(Long id, int stock);

    //Aumentar Stock
    void aumentarStock(Long id, int stock);
}
