package com.pfa.jobportal.model;

import com.pfa.jobportal.model.gestion.Candidature;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidat extends User {

    private String cv;
    private String photo;
    private boolean active = true;
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }



    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL)
    private List<Candidature> candidatures;
}
