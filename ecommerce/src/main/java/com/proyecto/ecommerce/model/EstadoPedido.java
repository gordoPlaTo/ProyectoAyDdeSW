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
@Table(name = "EstadoPedidos")
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadoPedido;

    @Column(nullable = false, unique = true, length = 35)
    private String descripcion;

    public EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    @OneToMany (mappedBy ="estadoPedido")
    private List<Pedido> listPedido;


}
