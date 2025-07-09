package com.pfa.jobportal.controller;

import com.pfa.jobportal.model.Recruteur;
import com.pfa.jobportal.model.gestion.Candidature;
import com.pfa.jobportal.model.gestion.Message;
import com.pfa.jobportal.model.gestion.Offre;
import com.pfa.jobportal.model.enumm.RoleExpediteur;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/recruteur")
public class RecruteurController {

    @Autowired private OffreService offreService;
    @Autowired private UserService userService;
    @Autowired private RecruteurService recruteurService;
    @Autowired private CandidatService candidatService;
    @Autowired private CandidatureService candidatureService;
    @Autowired private MessageService messageService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Recruteur recruteur = (Recruteur) userService.findByEmail(userDetails.getUsername());
        List<Offre> offres = offreService.findByRecruteur(recruteur);

        long nbMessagesNonLus = messageService.countNonLusPourRecruteur(recruteur.getId());
        model.addAttribute("nbMessagesNonLus", nbMessagesNonLus);

        model.addAttribute("recruteur", recruteur);
        model.addAttribute("nom", recruteur.getNom());
        model.addAttribute("logo", recruteur.getLogo());
        model.addAttribute("offre", new Offre());
        model.addAttribute("offres", offres);
        model.addAttribute("nbMessagesNonLus", nbMessagesNonLus); // 🔴 compteur de messages non lus

        return "recruteur/dashboard";
    }

    @PostMapping("/offres")
    public String saveOffre(@ModelAttribute Offre offre, @AuthenticationPrincipal UserDetails userDetails) {
        Recruteur recruteur = (Recruteur) userService.findByEmail(userDetails.getUsername());
        offre.setRecruteur(recruteur);
        offre.setDatePublication(LocalDate.now());
        offreService.createOffre(offre);
        return "redirect:/recruteur/dashboard";
    }

    @GetMapping("/offres/delete/{id}")
    public String delete(@PathVariable Long id) {
        offreService.deleteOffre(id);
        return "redirect:/recruteur/dashboard";
    }

    @GetMapping("/profil")
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Recruteur recruteur = (Recruteur) userService.findByEmail(userDetails.getUsername());
        model.addAttribute("recruteur", recruteur);
        return "recruteur/profil";
    }

    @PostMapping("/profil")
    public String updateProfile(@RequestParam("nom") String nom,
                                @RequestParam("description") String description,
                                @RequestParam("secteur") String secteur,
                                @RequestParam("localisation") String localisation,
                                @RequestParam("logoFile") MultipartFile logoFile,
                                @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        String uploadDir = System.getProperty("user.dir") + "/uploads";
        String logoPath = null;

        if (!logoFile.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String logoName = UUID.randomUUID() + "_" + logoFile.getOriginalFilename();
            File dest = new File(dir, logoName);
            logoFile.transferTo(dest);
            logoPath = "/uploads/" + logoName;
        }

        String mapLink = "https://www.google.com/maps/search/?api=1&query=" + localisation.trim().replace(" ", "+");
        recruteurService.updateProfile(userDetails.getUsername(), nom, description, secteur, mapLink, logoPath);
        return "redirect:/recruteur/profil?success";
    }
    @GetMapping("/candidatures")
    public String voirCandidatures(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Recruteur recruteur = (Recruteur) userService.findByEmail(userDetails.getUsername());
        List<Candidature> candidatures = candidatureService.findByRecruteurId(recruteur.getId());

        // 🔴 Ajoute cette ligne pour le badge
        long nbMessagesNonLus = messageService.countNonLusPourRecruteur(recruteur.getId());
        model.addAttribute("nbMessagesNonLus", nbMessagesNonLus);

        model.addAttribute("candidatures", candidatures);
        return "recruteur/candidatures";
    }

    @GetMapping("/candidature/accepter/{id}")
    public String accepterCandidature(@PathVariable Long id) {
        candidatureService.accepterCandidature(id);
        return "redirect:/recruteur/candidatures";
    }

    @GetMapping("/candidature/refuser/{id}")
    public String refuserCandidature(@PathVariable Long id) {
        candidatureService.refuserCandidature(id);
        return "redirect:/recruteur/candidatures";
    }

    @GetMapping("/info-candidat/{id}")
    public String afficherInfosCandidat(@PathVariable Long id, Model model) {
        Candidature candidature = candidatureService.findById(id);
        model.addAttribute("candidature", candidature);
        return "recruteur/information-candidat";
    }

    @PostMapping("/envoyer-date/{id}")
    public String envoyerDateEntretien(@PathVariable Long id,
                                       @RequestParam("dateEntretien") String dateEntretien) {
        Candidature candidature = candidatureService.findById(id);
        candidature.setDateEntretien(LocalDate.parse(dateEntretien));
        candidatureService.save(candidature);
        return "redirect:/recruteur/info-candidat/" + id + "?envoye";
    }

    @GetMapping("/chat/{candidatureId}")
    public String afficherChat(@PathVariable Long candidatureId, Model model) {
        List<Message> messages = messageService.getMessagesParCandidature(candidatureId);
        messageService.marquerCommeLus(messages);

        model.addAttribute("messages", messages);
        model.addAttribute("candidatureId", candidatureId);
        return "recruteur/chat"; // ✅ charge la page chat.html côté recruteur
    }


    @PostMapping("/chat/{candidatureId}")
    public String envoyerMessage(@PathVariable Long candidatureId,
                                 @RequestParam String contenu) {
        messageService.envoyerMessage(candidatureId, contenu, RoleExpediteur.RECRUTEUR);
        return "redirect:/recruteur/chat/" + candidatureId;
    }

    @GetMapping("/api/messages/{candidatureId}")
    @ResponseBody
    public List<Message> getMessagesJson(@PathVariable Long candidatureId) {
        return messageService.getMessages(candidatureId);
    }

    @PostMapping("/api/messages/{candidatureId}")
    @ResponseBody
    public void envoyerMessageJson(@PathVariable Long candidatureId,
                                   @RequestParam String contenu) {
        messageService.envoyerMessage(candidatureId, contenu, RoleExpediteur.RECRUTEUR);
    }

}
