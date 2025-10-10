package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.dto.ProductoMasVendidoDTO;
import com.proyecto.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido,Long> {
/*
    @Query("SELECT new com.proyecto.eccommerce.dto.ProductoVenta FROM PEDIDO p WHERE p.estado_pedido = 'completado'")
    Object[] ventasPedido();*/

/*
    @Query("""
            SELECT new com.proyecto.eccommerce.dto.ProductoMasVendidoDTO(p.nombre, SUM(dp.cantidad))
            FROM Producto p JOIN DetallePedido dp ON p.idProducto = dp.idDetalle
            GROUP BY p.idProducto, p.nombre
            ORDER BY UnidadesVendidas DESC
            LIMIT 1
            """)
    ProductoMasVendidoDTO productoMasVendido();*/
}
