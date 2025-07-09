package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.Statut;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Offre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends GenericRepository<Candidature, Long> {

    // 🔹 Obtenir toutes les candidatures d'une offre
    List<Candidature> findByOffre(Offre offre);

    // 🔹 Obtenir toutes les candidatures d’un candidat
    List<Candidature> findByCandidat(Candidat candidat);

    // 🔹 Obtenir les candidatures reçues par un recruteur
    @Query("SELECT c FROM Candidature c WHERE c.offre.recruteur.id = :recruteurId")
    List<Candidature> findByRecruteurId(@Param("recruteurId") Long recruteurId);

    // (optionnel) pour une combinaison spécifique offre+candidat
    Candidature findByOffreAndCandidat(Offre offre, Candidat candidat);
    long countByStatut(Statut statut);

}
