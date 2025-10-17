package com.proyecto.ecommerce.dto.PedidosDTO;

import org.springframework.web.multipart.MultipartFile;

public record ComprobanteReqDTO (
        Long idPedido,
        MultipartFile comprobante
){
}
