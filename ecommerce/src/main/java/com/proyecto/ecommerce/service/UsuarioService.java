package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements  IUsuarioService{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public Usuario obtenerUsuarioByEmail(String email) {
        return userRepository.findUserEntityByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+ email + " no fue encontrado."));
    }

    //Obtener la informacion de perfil

    //Modificar informacion de perfil

    //Obtener pedidos realizados






}
