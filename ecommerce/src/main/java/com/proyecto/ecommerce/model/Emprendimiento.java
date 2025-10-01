package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Emprendimiento")
public class Emprendimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmprendimiento;

    public Emprendimiento(List<Contacto> listContacto, String direccion, String titulo) {
        this.listContacto = listContacto;
        this.direccion = direccion;
        this.titulo = titulo;
    }

    @Column(length = 20)
    private String titulo;

    @Column(length = 120)
    private String descripcion;

    @Column(length = 120)
    private String direccion;
    
    @OneToMany(mappedBy = "emprendimiento",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contacto> listContacto;

}
