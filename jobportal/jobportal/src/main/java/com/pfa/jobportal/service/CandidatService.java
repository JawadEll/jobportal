package com.pfa.jobportal.service;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.Statut;

import java.util.List;

public interface CandidatService {
    void enregistrer(Candidat candidat);
    Candidat findByEmail(String email);
    Candidat findById(Long id);// ✅ à ajouter
    void save(Candidat candidat);
    long count();
    void deleteById(Long id);  // ➕ à ajouter

    List<Candidat> findAll();

}
