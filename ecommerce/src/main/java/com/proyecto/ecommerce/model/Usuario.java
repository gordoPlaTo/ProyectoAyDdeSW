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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 35)
    private String nombre;

    @Column(nullable = false, length = 35)
    private String apellido;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(unique = true, length = 254, nullable = false)
    private String email;

    @Column(nullable = false)
    private Long dni;

    @Column(nullable = false)
    private LocalDate fechaNac;

    @Column(nullable = false, length = 120 )
    private String direccion;

    private boolean acceptedTerms;

    private boolean enabled;//Habilitado o no por el usuario

    private boolean accountNotExpired;//Cuenta sin uso
    private boolean accountNotLocked;//Bloqueado por admin
    private boolean credentialNotExpired;//Casos para cambio de password periodico

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //Eager se encarga de cargar todos los roles
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> rolesList = new HashSet<>();

    @OneToMany(mappedBy = "pedido")
    private List<Pedido> listPedido;


}
