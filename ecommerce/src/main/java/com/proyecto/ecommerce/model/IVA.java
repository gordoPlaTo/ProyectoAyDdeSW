package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "IVA")
@NoArgsConstructor
public class IVA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIVA;

    @Column(nullable = false)
    private String categoria;


    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal porcentaje;

    public IVA(String categoria, BigDecimal porcentaje) {
        this.categoria = categoria;
        this.porcentaje = porcentaje;
    }


}
