package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.Statut;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatRepository extends GenericRepository<Candidat, Long> {

    Optional<Candidat>findByEmail (String email);

}
