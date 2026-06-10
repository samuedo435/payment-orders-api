package com.vortexbird.paymentorders.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Correo electrónico utilizado para autenticación.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Contraseña almacenada en formato encriptado.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rol asignado al usuario.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
