package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialReqDTO;
import com.proyecto.ecommerce.dto.MaterialesDTO.MaterialesPatchDTO;
import com.proyecto.ecommerce.model.Material;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IMaterialRepository;
import com.proyecto.ecommerce.utils.ConvText;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService implements IMaterialService{
    @Autowired
    private IMaterialRepository materialRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Material crearMaterial(MaterialReqDTO matDTO) {
        Material mat = new Material();
        mat.setNombre(matDTO.nombre().toLowerCase());
        mat.setDescripcion(matDTO.descripcion().toLowerCase());
        mat.setStock(matDTO.stock());
        mat.setUrlfoto(cloudinaryService.subirImagen(matDTO.imgMaterial(),"foto-Material"));

        return materialRepository.save(mat);
    }

    @Override
    public Material obtenerMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Algo sucedio mal y no se encontro el material con id " + id ));
    }

    @Override
    public List<Material> obtenerMateriales()
    {
        List<Material> listMat = materialRepository.findAll().stream()
                .peek(m -> m.setNombre(ConvText.toUpperWords(m.getNombre())))
                .toList();
        return materialRepository.findAll();
    }

    @Override
    public void modificarMaterial(Long id, MaterialesPatchDTO matDTO) {
        Material mat = this.obtenerMaterialById(id);
        boolean fueModificado = false;

        if(matDTO.nombre()!=null && !matDTO.nombre().isBlank()){
            mat.setNombre(matDTO.nombre());
            fueModificado = true;
        }
        if(matDTO.descripcion()!=null && !matDTO.descripcion().isBlank()){
            mat.setDescripcion(matDTO.descripcion());
            fueModificado = true;
        }

        if (!fueModificado){
            throw new IllegalArgumentException("Debe modificar al menos un campo");
        }

        materialRepository.save(mat);
    }

    @Override
    public void reducirStock(Long id, int stock) {
        Material mat = this.obtenerMaterialById(id);
        if (mat.getStock() < stock){
            throw new IllegalArgumentException("No puedes restar mas stock del que realmente dispones en tu inventario.");
        }
        mat.setStock(mat.getStock() - stock);
        materialRepository.save(mat);
    }

    @Override
    public void aumentarStock(Long id, int stock) {
        Material mat = this.obtenerMaterialById(id);
        mat.setStock(mat.getStock() + stock);
        materialRepository.save(mat);
    }

    @Override
    public Material borrarMaterial(Long id) {
        Material mat = this.obtenerMaterialById(id);
        materialRepository.delete(mat);
        return mat;
    }

    @Override
    public void modificarImagen(Long id, MultipartFile img) {
        Material mat = obtenerMaterialById(id);
        mat.setUrlfoto(cloudinaryService.subirImagen(img,"foto-Material"));

    }
}
