package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UsuarioService implements  IUsuarioService{
    //Este seria el service del modelo con el CRUD basico

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Usuario obtenerUsuarioByEmail(String email) {

        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("El usuario con el email especificado no fue encontrado."));
    }

    //Obtener la informacion de perfil

    //Modificar informacion de perfil

    //Obtener pedidos realizados






}
