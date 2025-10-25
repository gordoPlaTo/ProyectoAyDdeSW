package com.proyecto.ecommerce.config.Component;

import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IRoleRepository;
import com.proyecto.ecommerce.repository.IUserRepository;
import com.proyecto.ecommerce.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Component
@Order(3)
public class UsuarioInitializr implements CommandLineRunner {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() == 0) {
            try {
                Usuario user = new Usuario();

                Role rol = roleRepository.findById(2L)
                        .orElseThrow(()-> new RuntimeException("No se encontro el Rol para el usuario por defecto."));

                user.setUsername("DefUser");
                user.setApellido("DefApellido");
                String password = "MiClaveSegura#10";
                user.setPassword(userDetailsServiceImp.encriptPassword(password));
                user.setFechaNac(LocalDate.of(2025,10,23));
                user.setDireccion("Direccion por Defecto");
                user.setEmail("userdefault@gmail.com");
                user.setDni("00000000");
                user.setAcceptedTerms(true);
                user.setAccountNotLocked(true);
                user.setAccountNotExpired(true);
                user.setCredentialNotExpired(true);
                user.setEnabled(true);

                if (user.getRolesList() == null) {
                    user.setRolesList(new HashSet<>());
                }

                user.getRolesList().add(rol);

                userRepository.save(user);

            } catch (Exception e) {
                throw new RuntimeException("Hubo un error al cargar el usuario por Defecto", e);
            }

        }
    }
}
