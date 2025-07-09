package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.enumm.RoleExpediteur;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Message;
import com.pfa.jobportal.repository.MessageRepository;
import com.pfa.jobportal.service.CandidatureService;
import com.pfa.jobportal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CandidatureService candidatureService;
    @Override
    public List<Message> getMessagesParCandidature(Long candidatureId) {
        return messageRepository.findByCandidatureIdOrderByDateEnvoi(candidatureId);
    }

    @Override
    public void envoyerMessage(Long candidatureId, String contenu, RoleExpediteur role) {
        Candidature candidature = candidatureService.findById(candidatureId);
        Message message = new Message();
        message.setContenu(contenu);
        message.setDateEnvoi(LocalDateTime.now());
        message.setRole(role);
        message.setLu(false);
        message.setCandidature(candidature);
        messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(Long candidatureId) {
        return messageRepository.findByCandidatureIdOrderByDateEnvoiAsc(candidatureId);
    }

    @Override
    public long countNonLusPourRecruteur(Long recruteurId) {
        return messageRepository.countByCandidature_Offre_Recruteur_IdAndLuFalseAndRole(recruteurId, RoleExpediteur.CANDIDAT);
    }

    @Override
    public List<Message> getMessagesPourCandidat(Long candidatId) {
        return messageRepository.findByCandidature_Candidat_IdOrderByDateEnvoi(candidatId);
    }

    @Override
    public void marquerCommeLus(List<Message> messages) {
        messages.forEach(m -> m.setLu(true));
        messageRepository.saveAll(messages);
    }

    @Override
    public long countNonLusPourCandidat(Long candidatId) {
        return messageRepository.countByCandidature_Candidat_IdAndLuFalseAndRole(candidatId, RoleExpediteur.RECRUTEUR);
    }
}
