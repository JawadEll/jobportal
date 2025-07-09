package com.pfa.jobportal.model.gestion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pfa.jobportal.model.Recruteur;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "offres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 1000)
    private String description;

    private String localisation;

    private LocalDate datePublication;

    @ManyToOne
    @JoinColumn(name = "recruteur_id")
    @JsonBackReference
    private Recruteur recruteur;

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<Candidature> candidatures;
}
