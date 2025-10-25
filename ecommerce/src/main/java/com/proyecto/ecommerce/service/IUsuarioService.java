package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.UsuariosDTO.UserModDirecDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UserModPassDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UsuarioDatosDTO;
import com.proyecto.ecommerce.model.Usuario;

public interface IUsuarioService {
    //Modificar Datos personales
    void modificarDireccion(UserModDirecDTO direccion);

    void modificarPassword(UserModPassDTO password);

    UsuarioDatosDTO obtenerDatosPersonales();


    void deshabilitarCuenta();


    Usuario obtenerUsuarioByEmail();

}
