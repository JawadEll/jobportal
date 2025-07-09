package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.enumm.RoleExpediteur;
import com.pfa.jobportal.model.gestion.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByCandidatureIdOrderByDateEnvoi(Long candidatureId);

    List<Message> findByCandidature_Candidat_IdOrderByDateEnvoi(Long candidatId);
    long countByCandidature_Offre_Recruteur_IdAndLuFalseAndRole(Long recruteurId, RoleExpediteur role);

    long countByCandidature_Candidat_IdAndLuFalseAndRole(Long candidatId, RoleExpediteur role);

    List<Message> findByCandidatureIdOrderByDateEnvoiAsc(Long candidatureId);
}
