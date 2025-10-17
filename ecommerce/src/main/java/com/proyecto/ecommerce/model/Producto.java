package com.proyecto.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Productos")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false, length = 35)
    private String nombre;

    @Column(nullable = false, length = 300)
    private String descripcion;

    public Producto(String nombre, String descripcion, BigDecimal precio, int stock, IVA iva) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.iva = iva;
    }

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Column(name = "imgPedido")
    private String url;

    private boolean isEnable = true;

    @ManyToOne
    @JoinColumn (name="iva")
    private IVA iva;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> listDetallePedido;

}
