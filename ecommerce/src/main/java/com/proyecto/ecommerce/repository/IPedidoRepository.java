package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO;
import com.proyecto.ecommerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido,Long> {


    @Query("""
        SELECT new com.proyecto.ecommerce.dto.ProductosDTO.ProductoVentaDTO(
            p.nombre,
            SUM(dp.precioTotal),
            SUM(dp.cantidad),
            SUM(dp.precioNeto)
        )
        FROM Pedido ped
        JOIN ped.listDetallePedido dp
        JOIN dp.producto p
        WHERE ped.estadoPedido.descripcion = 'Completado'
        GROUP BY p.nombre
    """)
    List<ProductoVentaDTO> ventasPedido();


    @Query("""
           SELECT ped
           FROM Pedido ped
           JOIN FETCH ped.usuario usr
           JOIN FETCH ped.estadoPedido est
           LEFT JOIN FETCH ped.listDetallePedido dp
           WHERE ped.idPedido = :idPedido
             AND usr.email = :email
           """)
    Optional<Pedido> obtenerPedidoPorIdYEmail(
            @Param("idPedido") Long idPedido,
            @Param("email") String email
    );


    @Query("""
        SELECT DISTINCT ped
        FROM Pedido ped
        JOIN FETCH ped.usuario usr
        JOIN FETCH ped.estadoPedido est
        LEFT JOIN FETCH ped.listDetallePedido dp
        WHERE usr.email = :email
        """)
    List<Pedido> obtenerPedidosPorEmail(@Param("email") String email);


    @Query("""
        SELECT DISTINCT ped
        FROM Pedido ped
        WHERE ped.estadoPedido.descripcion = 'Espera de Pago'
    """)
    List<Pedido> obtenerPendientesPago();


    @Query("""
        SELECT DISTINCT ped
        FROM Pedido ped
        WHERE ped.estadoPedido.descripcion = 'En Tramite'
    """)
    List<Pedido> obtenerPedidoEnTramite();
}
