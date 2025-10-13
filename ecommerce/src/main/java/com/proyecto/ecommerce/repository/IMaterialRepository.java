package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMaterialRepository extends JpaRepository<Material,Long> {
}
