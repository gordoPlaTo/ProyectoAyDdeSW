package com.proyecto.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Users")
@Getter@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 35)
    private String username;

    @Column(nullable = false, length = 35)
    private String apellido;

    @Column(nullable = false, length = 60)
    private String password;

    public Usuario(String username, String apellido, String password, String email, String dni, LocalDate fechaNac, String direccion, boolean acceptedTerms) {
        this.username = username;
        this.apellido = apellido;
        this.password = password;
        this.email = email;
        this.dni = dni;
        this.fechaNac = fechaNac;
        this.direccion = direccion;
        this.acceptedTerms = acceptedTerms;

    }

    @Column(unique = true, length = 254, nullable = false)
    private String email;

    @Column(nullable = false, length = 8)
    private String dni;

    @Column(nullable = false)
    private LocalDate fechaNac;

    @Column(nullable = false, length = 120 )
    private String direccion;

    private boolean acceptedTerms;

    private boolean enabled = true;

    //Los Siguientes atributos son exigidos por el framework
    //No sea mapean en la Bd gracias a la siguiente anotacion
    @Transient
    private boolean accountNotExpired = true;
    @Transient
    private boolean accountNotLocked = true;
    @Transient
    private boolean credentialNotExpired = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //Eager se encarga de cargar todos los roles
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> rolesList = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> listPedido = new ArrayList<>();


}
