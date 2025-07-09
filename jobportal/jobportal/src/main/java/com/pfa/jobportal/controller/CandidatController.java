package com.pfa.jobportal.controller;

import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.enumm.RoleExpediteur;
import com.pfa.jobportal.model.enumm.Statut;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Message;
import com.pfa.jobportal.model.gestion.Offre;
import com.pfa.jobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidat")
public class CandidatController {

    @Autowired private CandidatService candidatService;
    @Autowired private OffreService offreService;
    @Autowired private CandidatureService candidatureService;
    @Autowired private MessageService messageService; // ✅ Ajouté pour messages non lus

    // ✅ Tableau de bord
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        long nbMessagesNonLus = messageService.countNonLusPourCandidat(candidat.getId());

        model.addAttribute("candidat", candidat);
        model.addAttribute("nbMessagesNonLus", nbMessagesNonLus);

        List<Candidature> candidatures = candidatureService.findByCandidat(candidat);
        boolean hasAccepted = candidatures.stream()
                .anyMatch(c -> c.getStatut() == Statut.ACCEPTEE);
        if (hasAccepted) {
            model.addAttribute("successMessage", "🎉 Félicitations ! Une de vos candidatures a été acceptée.");
        }

        return "candidat/dashboard";
    }

    @GetMapping("/messages")
    public String voirMessages(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        List<Message> messages = messageService.getMessagesPourCandidat(candidat.getId());

        // ✅ Marquer comme lus tous les messages reçus du recruteur
        messageService.marquerCommeLus(messages);

        model.addAttribute("candidat", candidat);
        model.addAttribute("messages", messages);
        return "candidat/messages";  // ➕ Tu dois créer cette page HTML
    }


    @GetMapping("/profil")
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        model.addAttribute("candidat", candidat);
        return "candidat/profile";
    }

    @PostMapping("/profil/save")
    public String saveProfil(@RequestParam("id") Long id,
                             @RequestParam("cvFile") MultipartFile cvFile,
                             @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
        Candidat candidat = candidatService.findById(id);
        String uploadDir = "C:/Users/HP/Desktop/Emsi4iir/SEMESTRE2/Pfa/jobportal/uploads/";

        if (!cvFile.isEmpty()) {
            String cvName = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
            cvFile.transferTo(new File(uploadDir + cvName));
            candidat.setCv(cvName);
        }

        if (!photoFile.isEmpty()) {
            String photoName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            photoFile.transferTo(new File(uploadDir + photoName));
            candidat.setPhoto(photoName);
        }

        candidatService.save(candidat);
        return "redirect:/candidat/profil?success";
    }

    @GetMapping("/candidatures")
    public String voirCandidatures(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        List<Candidature> candidatures = candidatureService.findByCandidat(candidat)
                .stream()
                .sorted(Comparator.comparing(Candidature::getStatut))
                .collect(Collectors.toList());

        model.addAttribute("candidat", candidat);
        model.addAttribute("candidatures", candidatures);
        return "candidat/candidatures";
    }
    // ✅ Le candidat répond à un message
    @PostMapping("/messages/{candidatureId}/envoyer")
    public String repondreMessage(@PathVariable Long candidatureId,
                                  @RequestParam String contenu) {
        messageService.envoyerMessage(candidatureId, contenu, RoleExpediteur.CANDIDAT);
        return "redirect:/candidat/messages";
    }

    @GetMapping("/offres")
    public String voirOffres(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        List<Offre> offres = offreService.findAll();
        model.addAttribute("offres", offres);
        model.addAttribute("candidat", candidat);
        return "candidat/offres";
    }
    @GetMapping("/chat/{candidatureId}")
    public String afficherChatCandidat(@PathVariable Long candidatureId, Model model) {
        List<Message> messages = messageService.getMessagesParCandidature(candidatureId);
        messageService.marquerCommeLus(messages);

        model.addAttribute("messages", messages);
        model.addAttribute("candidatureId", candidatureId);
        return "candidat/chat"; // page à créer
    }


    @GetMapping("/postuler/{id}")
    public String postuler(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Candidat candidat = candidatService.findByEmail(userDetails.getUsername());
        Offre offre = offreService.findById(id);
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        model.addAttribute("candidature", candidature);
        return "candidat/postuler";
    }

    @PostMapping("/postuler/save")
    public String saveCandidature(@ModelAttribute Candidature candidature) {
        if (candidature.getStatut() == null) {
            candidature.setStatut(Statut.EN_ATTENTE);
        }
        candidatureService.save(candidature);
        return "redirect:/candidat/candidatures";
    }

    @PostMapping("/candidature/{id}/demande-date")
    public String demanderDate(@PathVariable Long id) {
        Candidature candidature = candidatureService.findById(id);
        candidature.setMessage(candidature.getMessage() + "\nLe candidat a demandé une date.");
        candidatureService.save(candidature);
        return "redirect:/candidat/candidatures?demandeEnvoyee";
    }

}
