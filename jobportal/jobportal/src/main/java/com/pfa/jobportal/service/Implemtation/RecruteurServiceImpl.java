package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.Recruteur;
import com.pfa.jobportal.repository.RecruteurRepository;
import com.pfa.jobportal.service.RecruteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruteurServiceImpl implements RecruteurService {

    @Autowired
    private RecruteurRepository recruteurRepository;

    @Override
    public Recruteur updateProfile(String email, String nom, String description,
                                   String secteur, String localisation, String logoPath) {
        Recruteur recruteur = recruteurRepository.findByEmail(email);
        recruteur.setNom(nom);
        recruteur.setDescription(description);
        recruteur.setSecteur(secteur);
        recruteur.setLocalisation(localisation);
        if (logoPath != null) {
            recruteur.setLogo(logoPath);
        }
        return recruteurRepository.save(recruteur);
    }

    @Override
    public List<Recruteur> findAll() {
        return recruteurRepository.findAll();
    }

    @Override
    public Recruteur findById(Long id) {
        return recruteurRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Recruteur recruteur) {
        recruteurRepository.save(recruteur);
    }

    @Override
    public void deleteById(Long id) {
        recruteurRepository.deleteById(id);
    }
}
