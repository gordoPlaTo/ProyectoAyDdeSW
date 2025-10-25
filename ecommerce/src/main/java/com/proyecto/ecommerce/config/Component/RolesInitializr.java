package com.proyecto.ecommerce.config.Component;

import com.proyecto.ecommerce.model.Permission;
import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.repository.IPermissionRepository;
import com.proyecto.ecommerce.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(2)
public class RolesInitializr implements CommandLineRunner {
    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionRepository permissionRepository;


    @Override
    public void run(String... args) throws Exception {

        if (permissionRepository.count() == 0 && roleRepository.count() == 0) {

            try {
                List<Permission> listPermsCliente = Arrays.asList(
                        new Permission("CLIENTE_CREATE"),
                        new Permission("CLIENTE_UPDATE"),
                        new Permission("CLIENTE_DELETE"),
                        new Permission("CLIENTE_READ")
                );

                Set<Permission> listPermisosCliente = new HashSet<>(listPermsCliente);

                List<Permission> listPermsAdmin = Arrays.asList(
                        new Permission("ADMIN_CREATE"),
                        new Permission("ADMIN_UPDATE"),
                        new Permission("ADMIN_DELETE"),
                        new Permission("ADMIN_READ")
                );
                Set<Permission> listPermisosAdmin = new HashSet<>(listPermsAdmin);

                Role rolCliente = new Role("CLIENTE", listPermisosCliente);
                Role rolAdmin = new Role("ADMIN", listPermisosAdmin);

                roleRepository.save(rolCliente);
                roleRepository.save(rolAdmin);


            } catch (RuntimeException e) {
                throw new RuntimeException("Hubo un error al cargar los Roles y Permisos " + e);

            }
        }
    }
}
