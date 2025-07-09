package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.Statut;
import com.pfa.jobportal.repository.CandidatRepository;
import com.pfa.jobportal.service.CandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatServiceImpl implements CandidatService {

    @Autowired
    private CandidatRepository candidatRepository;

    @Override
    public void enregistrer(Candidat candidat) {
        candidatRepository.save(candidat);
    }

    @Override
    public List<Candidat> findAll() {
        return candidatRepository.findAll();
    }

    @Override
    public Candidat findByEmail(String email) {
        return candidatRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Candidat findById(Long id) {
        return candidatRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteById(Long id) {
        candidatRepository.deleteById(id);
    }


    @Override
    public void save(Candidat candidat) {
        candidatRepository.save(candidat);
    }

    // ✅ Implémentation de count()
    @Override
    public long count() {
        return candidatRepository.count();
    }

}
