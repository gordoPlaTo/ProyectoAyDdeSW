package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Emprendimiento")
public class Emprendimiento {
    @Id
    private Long idEmprendimiento;

    @Column(length = 35)
    private String titulo;

    @Column(length = 800)
    private String descripcion;

    @Column(length = 120)
    private String direccion;

    @Column(length = 11)
    private String cuit;

    @Column(unique = true, length = 254, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isMod;
    
    @OneToMany(mappedBy = "emprendimiento",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contacto> listContacto = new ArrayList<>();

}
