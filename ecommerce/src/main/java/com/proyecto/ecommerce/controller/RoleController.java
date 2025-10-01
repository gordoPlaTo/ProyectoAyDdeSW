package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.model.Permission;
import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.repository.IPermissionRepository;
import com.proyecto.ecommerce.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private IRoleRepository roleService;

    @Autowired
    private IPermissionRepository permiService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/crear")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        Set<Permission> permiList = new HashSet<Permission>();
        Permission readPermission;

        //Recuperar la Permission/s por su ID
        for (Permission per : role.getPermissionsList()) {
            readPermission = permiService.findById(per.getId()).orElse(null);
            if (readPermission != null){
                //Si encuentra, guardo en la lista
                permiList.add(readPermission);
            }
        }

        role.setPermissionsList(permiList);
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }

}
