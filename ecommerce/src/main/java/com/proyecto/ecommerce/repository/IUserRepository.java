package com.proyecto.ecommerce.repository;

import com.proyecto.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUserRepository extends JpaRepository <Usuario, Long> {
    Optional<Usuario> findUserEntityByEmail(String email);


}
