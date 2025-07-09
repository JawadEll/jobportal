package com.pfa.jobportal.service;

import com.pfa.jobportal.model.enumm.RoleExpediteur;
import com.pfa.jobportal.model.gestion.Message;

import java.util.List;

public interface MessageService {
    void envoyerMessage(Long candidatureId, String contenu, RoleExpediteur role);
    List<Message> getMessages(Long candidatureId);
    List<Message> getMessagesPourCandidat(Long candidatId);
    void marquerCommeLus(List<Message> messages);
    long countNonLusPourCandidat(Long candidatId);
    List<Message> getMessagesParCandidature(Long candidatureId);

    long countNonLusPourRecruteur(Long id);
}
