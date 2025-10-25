package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoPedidoRepository extends JpaRepository<EstadoPedido,Long> {
}
