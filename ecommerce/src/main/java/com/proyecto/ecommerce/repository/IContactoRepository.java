package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContactoRepository extends JpaRepository<Contacto,Long> {

}
