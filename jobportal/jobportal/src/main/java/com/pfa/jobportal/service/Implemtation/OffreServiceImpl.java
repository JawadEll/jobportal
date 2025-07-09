package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.Recruteur;
import com.pfa.jobportal.model.gestion.Offre;
import com.pfa.jobportal.repository.OffreRepository;
import com.pfa.jobportal.service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreServiceImpl implements OffreService {

    @Autowired
    private OffreRepository offreRepository;

    @Override
    public Offre createOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    @Override
    public List<Offre> findByRecruteur(Recruteur recruteur) {
        return offreRepository.findByRecruteur(recruteur);
    }
    @Override
    public long count() {
        return offreRepository.count();
    }

    @Override
    public Offre findById(Long id) {
        return offreRepository.findById(id).orElse(null);
    }
    @Override
    public List<Offre> findAll() {
        return offreRepository.findAll();
    }


    @Override
    public void deleteOffre(Long id) {
        offreRepository.deleteById(id);
    }
}
