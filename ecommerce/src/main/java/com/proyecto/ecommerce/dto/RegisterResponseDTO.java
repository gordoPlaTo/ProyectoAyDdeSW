package com.proyecto.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder
public record RegisterResponseDTO (String Resp,
                                   boolean status){
}
