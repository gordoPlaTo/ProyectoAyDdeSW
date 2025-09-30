package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.Emprendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpRepository extends JpaRepository<Emprendimiento,Long> {
}
