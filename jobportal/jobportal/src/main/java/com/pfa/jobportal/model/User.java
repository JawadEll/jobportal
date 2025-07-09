package com.pfa.jobportal.model;

import com.pfa.jobportal.model.enumm.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String email;

    @Column(name = "mot_de_passe")
    private String motDePasse;


    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(nullable = false)
    private boolean active = true;


}