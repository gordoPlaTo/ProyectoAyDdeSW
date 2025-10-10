package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.IVA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIvaRepository extends JpaRepository<IVA,Long>
{
}
