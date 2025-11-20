package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.*;
import com.proyecto.ecommerce.dto.authDTO.AuthLoginRequestDTO;
import com.proyecto.ecommerce.dto.authDTO.AuthResponseDTO;
import com.proyecto.ecommerce.dto.authDTO.RegisterRequestDTO;
import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IRoleRepository;
import com.proyecto.ecommerce.repository.IUserRepository;
import com.proyecto.ecommerce.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("El usuario con el email especificado no fue encontrado."));

        List<GrantedAuthority> authorityList = new ArrayList<>();

        usuario.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        usuario.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(
                usuario.getEmail(), //Usamos el Email como identificador principal en vez de username
                usuario.getPassword(),
                usuario.isEnabled(),
                //Los siguientes tres estados los exige el framework, pero solo usaremos
                //a nivel practico isEnable()
                usuario.isAccountNotExpired(),
                usuario.isCredentialNotExpired(),
                usuario.isAccountNotLocked(),

                authorityList);
    }



    public Authentication authenticate (String email, String password){
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null){
            throw  new BadCredentialsException("Invalid email or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid enail or password");
        }

        if (!userDetails.isEnabled()){
            throw new DisabledException("La cuenta está deshabilitada");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    public AuthResponseDTO loginUserEmail (@RequestBody @Valid AuthLoginRequestDTO authLoginRequestDTO){
        String email = authLoginRequestDTO.email().toLowerCase();
        String password = authLoginRequestDTO.password();

        Authentication authentication = this.authenticate(email,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accesToken = jwtUtils.createToken(authentication);

        Usuario user = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la cuenta con el email que especificaste."));
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(email,user.getUrlPerfil(),
                "Autenticacion Realizada con Exito", accesToken, true);
        return authResponseDTO;

    }

    @Autowired
    private IRoleRepository roleRepo;


    //Aca es posible crear un metodo para registrar y tener ya todo en USerDetails
    public RespDTO register(RegisterRequestDTO registerRequestDTO, Long idRol){
        String username = registerRequestDTO.nombre().toLowerCase();
        String apellido = registerRequestDTO.apellido().toLowerCase();
        String password = this.encriptPassword(registerRequestDTO.password());
        String email = registerRequestDTO.email().toLowerCase();
        String dni = registerRequestDTO.dni();
        LocalDate fechaNac = registerRequestDTO.fechaNac();
        String direccion = registerRequestDTO.direccion().toLowerCase();
        boolean acceptTerms = registerRequestDTO.acceptTerms();


        if (userRepository.findUserEntityByEmail(email).isPresent()){
            return new RespDTO("Ya existe un usuario registrado con ese email.",
                    false, LocalDateTime.now());
        }


        if (!acceptTerms){
            return new RespDTO("Para poder registrar una cuenta, el usuario adherente al contrato" +
                    " de cuenta debe aceptar nuestros terminos y condiciones.",
                    false, LocalDateTime.now());
        }

        Role role = roleRepo.findById(idRol)
                .orElse(null);
        Set<Role> roleList = new HashSet<>();

        if (role != null){
            Usuario usuario = new Usuario(username,apellido,password,email,dni,fechaNac,direccion, true);
            roleList.add(role);
            usuario.setRolesList(roleList);
            userRepository.save(usuario);

            return new RespDTO("Se completo el Registro satisfactoriamente, el usuario " + usuario.getUsername()
                    + " con el email " + email + " se encuentra activo para usar nuestra web.", true, LocalDateTime.now());
        }

        return new RespDTO("Algo sucedio mal y no se concreto el registro de la cuenta",
                false, LocalDateTime.now());
    }


    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
