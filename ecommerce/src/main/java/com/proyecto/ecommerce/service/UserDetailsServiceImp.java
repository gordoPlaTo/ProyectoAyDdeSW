package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.AuthLoginRequestDTO;
import com.proyecto.ecommerce.dto.AuthResponseDTO;
import com.proyecto.ecommerce.dto.RegisterRequestDTO;
import com.proyecto.ecommerce.dto.RegisterResponseDTO;
import com.proyecto.ecommerce.model.Role;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IRoleRepository;
import com.proyecto.ecommerce.repository.IUserRepository;
import com.proyecto.ecommerce.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+ email + " no fue encontrado."));

        List<GrantedAuthority> authorityList = new ArrayList<>();

        usuario.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        usuario.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(
                usuario.getEmail(), //Usamos el Email como identificador principal en ves del username
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
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    public AuthResponseDTO loginUserEmail (AuthLoginRequestDTO authLoginRequestDTO){
        String email = authLoginRequestDTO.email();
        String password = authLoginRequestDTO.password();

        Authentication authentication = this.authenticate(email,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accesToken = jwtUtils.createToken(authentication);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(email,"Autenticacion Realizada con Exito", accesToken, true);
        return authResponseDTO;

    }

    @Autowired
    private IRoleRepository roleRepo;

    //Aca es posible crear un metodo para registrar y tener ya todo en USerDetails
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO, Long idRol){
        String username = registerRequestDTO.nombre();
        String apellido = registerRequestDTO.apellido();
        String password = this.encriptPassword(registerRequestDTO.password());
        String email = registerRequestDTO.email();
        Long dni = registerRequestDTO.dni();
        LocalDate fechaNac = registerRequestDTO.fechaNac();
        String direccion = registerRequestDTO.direccion();
        boolean acceptTerms = registerRequestDTO.acceptTerms();

        Role role = roleRepo.findById(idRol)
                .orElse(null);
        Set<Role> roleList = new HashSet<>();

        if (role != null){
            Usuario usuario = new Usuario(username,apellido,password,email,dni,fechaNac,direccion,acceptTerms,true,true,true,true);
            roleList.add(role);
            usuario.setRolesList(roleList);
            userRepository.save(usuario);

            return new RegisterResponseDTO("Se completo el Registro satisfactoriamente, el usuario " + usuario.getUsername()
                    + " con el email " + email + " se encuentra activo para usar nuestra web.", true);
        }

        return new RegisterResponseDTO("Algo sucedio mal y no se concreto el registro de la cuenta",
                false);
    }


    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
