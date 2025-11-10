package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Materiales")
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    @Column(nullable = false,length = 35)
    private String nombre;
    @Column(nullable = false,length = 450)
    private String descripcion;
    @Column(nullable = false)
    private int stock;

}
