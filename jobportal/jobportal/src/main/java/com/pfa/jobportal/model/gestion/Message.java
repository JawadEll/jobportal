package com.pfa.jobportal.model.gestion;

import com.pfa.jobportal.model.enumm.RoleExpediteur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String contenu;

    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleExpediteur role;

    @Column(nullable = false)
    private boolean lu = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;

    // ✅ Optionnel : Setters manuels si besoin
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setRole(RoleExpediteur role) {
        this.role = role;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public void setCandidature(Candidature candidature) {
        this.candidature = candidature;
    }
}
