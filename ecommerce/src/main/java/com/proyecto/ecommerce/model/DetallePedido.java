package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "DetallePedidos")
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @Column(nullable = false)
    private int cantidad;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioNeto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal montoIva;

    @Column( name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioTotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public DetallePedido(int cantidad, Pedido pedido, BigDecimal precioNeto, BigDecimal montoIva, BigDecimal precioTotal, Producto producto) {
        this.cantidad = cantidad;
        this.pedido = pedido;
        this.precioNeto = precioNeto;
        this.montoIva = montoIva;
        this.precioTotal = precioTotal;
        this.producto = producto;
    }
}
