package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.Recruteur;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruteurRepository extends GenericRepository<Recruteur, Long> {

    // ✅ Trouver un recruteur par email (utilisé pour la mise à jour de profil)
    Recruteur findByEmail(String email);
}
