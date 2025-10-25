package com.proyecto.ecommerce.dto.MaterialesDTO;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record MaterialReqDTO (
        @NotBlank(message = "El nombre del material es obligatorio")
        @Size(min = 3, max = 35, message = "El nombre debe tener entre 3 y 35 caracteres")
        @Pattern( regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s'-]+$",
                message = "Solo se permiten letras, números, espacios, guiones y apóstrofes." )
        String nombre,

        @NotBlank(message = "Ingresar una descripcion es obligatorio")
        @Size(min = 15, max = 300, message = "La descripcion debe tener entre 15 y 120 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s@._\\-=',()]+$",
                message = "Solo se permiten letras, números, espacios, guiones, apóstrofes, comas, puntos, arrobas y paréntesis.")
        String descripcion,

        @Min(value = 0, message = "El stock no puede ser negativo")
        int stock,

        MultipartFile imgMaterial

) {
}


