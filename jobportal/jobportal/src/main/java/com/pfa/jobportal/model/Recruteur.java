package com.pfa.jobportal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pfa.jobportal.model.gestion.Offre;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "recruteurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Recruteur extends User {

    // ✅ Logo (chemin vers fichier local /uploads/...)
    private String logo;

    // ✅ Description de l'entreprise
    @Column(length = 1000)
    private String description;

    // ✅ Localisation géographique
    private String localisation;

    private boolean active = true;
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }


    //@Column(nullable = false)
    private String secteur;

    @OneToMany(mappedBy = "recruteur", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Offre> offres;

}
