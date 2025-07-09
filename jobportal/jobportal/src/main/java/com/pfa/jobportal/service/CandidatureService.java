package com.pfa.jobportal.service;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.Statut;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Offre;

import java.util.List;

public interface CandidatureService {
    Candidature postuler(Candidature candidature);
    List<Candidature> findByOffre(Offre offre);
    List<Candidature> findByCandidat(Candidat candidat);
    void supprimer(Long id);
    void save (Candidature candidature);
    Candidature findById(Long id);
    List<Candidature> findByRecruteurId(Long recruteurId);
    void accepterCandidature(Long id);
    void refuserCandidature(Long id);
    long count();
    long countByStatut(Statut statut);


}
