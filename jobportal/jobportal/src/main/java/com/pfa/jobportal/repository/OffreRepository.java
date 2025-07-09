package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.Recruteur;
import com.pfa.jobportal.model.gestion.Offre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends GenericRepository<Offre, Long> {
    List<Offre> findByRecruteur(Recruteur recruteur);
}
