package com.proyecto.ecommerce.dto.PedidosDTO;

import jakarta.validation.constraints.*;

public record PedidoCompletarDTO (

        @NotNull(message = "El id del pedido es obligatorio")
        @Positive(message = "El id debe ser un número positivo")
        Long id,

        @NotBlank(message = "El email es obligatorio")
        @Size(min = 10, max = 254, message = "El email debe tener entre 10 y 254 caracteres")
        @Email(message = "El formato del email no es válido")
        String email
){
}
