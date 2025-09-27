package com.proyecto.ecommerce.service;

import com.proyecto.ecommerce.dto.AuthLoginRequestDTO;
import com.proyecto.ecommerce.dto.AuthResponseDTO;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUserRepository;
import com.proyecto.ecommerce.utils.JwtUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+ username + " no fue encontrado."));

        List<GrantedAuthority> authorityList = new ArrayList<>();

        usuario.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        usuario.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(usuario.getUsername(),
                usuario.getPassword(),
                usuario.isEnabled(),
                //Los siguientes tres estados los exige el framework, pero solo usaremos
                //a nivel practico isEnable()
                usuario.isAccountNotExpired(),
                usuario.isCredentialNotExpired(),
                usuario.isAccountNotLocked(),

                authorityList);
    }



    public Authentication authentication (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw  new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid username or password");
        }

        if (!userDetails.isEnabled()){
            throw new DisabledException("La cuenta está deshabilitada");
        }

        return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
    }

    public AuthResponseDTO authResponseDTO (AuthLoginRequestDTO authLoginRequestDTO){
        String username = authLoginRequestDTO.usuario();
        String password = authLoginRequestDTO.password();

        Authentication authentication = this.authentication(username,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accesToken = jwtUtils.createToken(authentication);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username,"Autenticacion Realizada con Exito", accesToken, true);
        return authResponseDTO;

    }
}
