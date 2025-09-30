package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Contactos")
@NoArgsConstructor
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContacto;

    @Column(nullable = false, length = 60)
    private String descripcion;

    public Contacto(String descripcion) {
        this.emprendimiento.setIdEmprendimiento(1L);
        this.descripcion = descripcion;
    }

    @ManyToOne
    @JoinColumn(name = "emprendimiento")
    private Emprendimiento emprendimiento;

}
