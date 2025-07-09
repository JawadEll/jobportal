package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Offre;
import com.pfa.jobportal.repository.CandidatureRepository;
import com.pfa.jobportal.service.CandidatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.pfa.jobportal.model.enumm.Statut;

@Service
public class CandidatureServiceImpl implements CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Override
    public Candidature postuler(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    @Override
    public List<Candidature> findByOffre(Offre offre) {
        return candidatureRepository.findByOffre(offre);
    }

    @Override
    public List<Candidature> findByCandidat(Candidat candidat) {
        return candidatureRepository.findAll().stream()
                .filter(c -> c.getCandidat().equals(candidat))
                .toList(); // Si tu veux plus tard ajouter findByCandidat() dans le repo
    }

    @Override
    public void supprimer(Long id) {
        candidatureRepository.deleteById(id);
    }

    @Override
    public Candidature findById(Long id) {
        return candidatureRepository.findById(id).orElse(null);
    }
    @Override
    public void save(Candidature candidature) {
        candidature.setDateCandidature(LocalDate.now());
        if (candidature.getStatut() == null) {
            candidature.setStatut(Statut.EN_ATTENTE);
        }
        candidatureRepository.save(candidature);
    }

    @Override
    public void accepterCandidature(Long id) {
        Candidature c = candidatureRepository.findById(id).orElseThrow();
        c.setStatut(Statut.ACCEPTEE);
        candidatureRepository.save(c);
    }

    @Override
    public void refuserCandidature(Long id) {
        Candidature c = candidatureRepository.findById(id).orElseThrow();
        c.setStatut(Statut.REFUSEE);
        candidatureRepository.save(c);
    }
    @Override
    public List<Candidature> findByRecruteurId(Long recruteurId) {
        return candidatureRepository.findByRecruteurId(recruteurId);
    }
    @Override
    public long count() {
        return candidatureRepository.count();
    }

    @Override
    public long countByStatut(Statut statut) {
        return candidatureRepository.countByStatut(statut);
    }

}
