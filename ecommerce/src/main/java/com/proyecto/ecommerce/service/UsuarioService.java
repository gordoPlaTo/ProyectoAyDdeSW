package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.UsuariosDTO.UserModDirecDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UserModPassDTO;
import com.proyecto.ecommerce.dto.UsuariosDTO.UsuarioDatosDTO;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UsuarioService implements  IUsuarioService{
    //Este seria el service del modelo con el CRUD basico

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Usuario obtenerUsuarioByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("El usuario con el email especificado no fue encontrado."));
    }

    @Override
    public UsuarioDatosDTO obtenerDatosPersonales() {
        Usuario user = this.obtenerUsuarioByEmail();

        return new UsuarioDatosDTO(user.getUsername(),
                user.getApellido(),
                user.getEmail(),
                user.getDni(),
                user.getDireccion(),
                user.getUrlPerfil());
    }

    @Override
    public void modificarDireccion(UserModDirecDTO direccion) {
        Usuario usuario = obtenerUsuarioByEmail();
        usuario.setDireccion(direccion.direccion());
        userRepository.save(usuario);
    }

    @Override
    public void modificarPassword(UserModPassDTO password) {
        Usuario usuario = this.obtenerUsuarioByEmail();
        if (!passwordEncoder.matches(password.passwordOld(), usuario.getPassword())){
            throw new IllegalArgumentException("La contraseña actual que ingresaste es incorrecta.");
        }

        try {
            usuario.setPassword(userDetailsService.encriptPassword(password.passwordNew()));
            userRepository.save(usuario);
        }catch (Exception e){
            throw new RuntimeException("Se produjo un error al intentar guardar la contraseña.");
        }
    }

    @Override
    public void deshabilitarCuenta() {
        Usuario usuario = this.obtenerUsuarioByEmail();
        usuario.setEnabled(false);
        userRepository.save(usuario);
    }

    @Override
    public void modificarImagenPefil(MultipartFile img) {
        Usuario user = obtenerUsuarioByEmail();
        user.setUrlPerfil(cloudinaryService.subirImagen(img,"foto-Perfil"));

        userRepository.save(user);

    }
}
