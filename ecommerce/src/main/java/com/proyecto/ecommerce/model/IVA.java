package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IVA")
@NoArgsConstructor
@AllArgsConstructor
public class IVA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIVA;

    @Column(nullable = false)
    private String categoria;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal porcentaje;
    
}
