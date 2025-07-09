package com.pfa.jobportal.service;

import com.pfa.jobportal.model.Recruteur;
import com.pfa.jobportal.model.gestion.Offre;

import java.util.List;

public interface OffreService {
    Offre createOffre(Offre offre);
    List<Offre> findByRecruteur(Recruteur recruteur);
    Offre findById(Long id);
    void deleteOffre(Long id);
    long count();  // interface

    List<Offre> findAll();
}
