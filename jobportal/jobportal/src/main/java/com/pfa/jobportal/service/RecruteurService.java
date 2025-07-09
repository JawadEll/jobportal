package com.pfa.jobportal.service;

import com.pfa.jobportal.model.Recruteur;

import java.util.List;

public interface RecruteurService {
    Recruteur updateProfile(String email, String nom, String description,
                            String secteur, String localisation, String logoPath);
    List<Recruteur> findAll();
    Recruteur findById(Long id);
    void save(Recruteur recruteur);
    void deleteById(Long id);

}
