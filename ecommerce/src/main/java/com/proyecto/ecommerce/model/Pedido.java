package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Pedidos")
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private LocalDate FechaCreacion;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalCompra;

    //Esto representa en realidad una relacion N a M. Pero como la tabla intermedia resultante
    //requiere parametros adicionales se designa 1 a N con l a tabla intermedia
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> listDetallePedido;

    @ManyToOne
    @JoinColumn(name = "idPedido")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idPedido")
    private EstadoPedido estadoPedido;


}
