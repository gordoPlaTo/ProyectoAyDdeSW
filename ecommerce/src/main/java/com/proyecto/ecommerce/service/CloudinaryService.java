package com.proyecto.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary clodinary;

    //Limite de peso 10mb
    public String subirImagen(MultipartFile file, String folder){
        try {
            Map subirResultado = clodinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder",folder));
            return (String) subirResultado.get("secure_url");
        }catch (IOException e){
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }

}
