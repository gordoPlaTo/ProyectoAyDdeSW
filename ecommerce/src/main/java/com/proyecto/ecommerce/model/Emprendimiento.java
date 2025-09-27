package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Emprendimientos")
@NoArgsConstructor
@AllArgsConstructor
public class Emprendimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmprendimiento;

    @Column(nullable = true, length = 20)
    private String titulo;

    @Column(nullable = true, length = 650)
    private  String descripcion;

    @Column(nullable = true, length = 120)
    private String direccion;

    @OneToMany(mappedBy = "emprendimiento")
    private List<Contacto> listContacto;



}
