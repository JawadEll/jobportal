package com.pfa.jobportal.controller;

import com.pfa.jobportal.model.Admin;
import com.pfa.jobportal.model.enumm.Statut;
import com.pfa.jobportal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.pfa.jobportal.model.Candidat;
import com.pfa.jobportal.model.Recruteur;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CandidatService candidatService;

    @Autowired
    private RecruteurService recruteurService;


    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @Autowired
    private OffreService offreService;

    @Autowired
    private CandidatureService candidatureService;


    // ✅ Formulaire de création admin (seulement s'il n'existe pas déjà)
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (adminService.getLatestAdmin() != null) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("admin", new Admin());
        return "admin/create";
    }

    // ✅ Création de l'admin avec redirection vers login + message de succès
    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("admin") Admin admin,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            adminService.createAdmin(admin, imageFile);
            redirectAttributes.addFlashAttribute("success", "Administrateur créé avec succès. Veuillez vous connecter.");
            return "redirect:/admin/loginAdmin";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/create";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création de l'administrateur.");
            return "admin/create";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Admin admin = adminService.getLatestAdmin();

        long nbUsers = userService.count(); // compte tous les utilisateurs (Candidat + Recruteur)
        long nbOffres = offreService.count();
        long nbCandidatures = candidatureService.count();
        long nbCandidaturesEnAttente = candidatureService.countByStatut(Statut.EN_ATTENTE);
        model.addAttribute("nbCandidaturesEnAttente", nbCandidaturesEnAttente);

        long nbCandidaturesAcceptees = candidatureService.countByStatut(Statut.ACCEPTEE);
        long nbCandidaturesRefusees = candidatureService.countByStatut(Statut.REFUSEE);

        model.addAttribute("admin", admin);
        model.addAttribute("nbUsers", nbUsers);
        model.addAttribute("nbOffres", nbOffres);
        model.addAttribute("nbCandidatures", nbCandidatures);
        model.addAttribute("nbCandidaturesAcceptees", nbCandidaturesAcceptees);
        model.addAttribute("nbCandidaturesRefusees", nbCandidaturesRefusees);

        return "admin/dashboard";
    }

    @Controller
    @RequestMapping("/admin")
    public class AdminAuthController {

        @GetMapping("/loginAdmin")
        public String loginAdminPage() {
            return "admin/loginAdmin"; // fichier .html dans templates/admin/
        }
    }
/// coter activer ou delete user
@GetMapping("/utilisateurs")
public String listerUtilisateurs(Model model) {
    model.addAttribute("candidats", candidatService.findAll());
    model.addAttribute("recruteurs", recruteurService.findAll());
    return "admin/utilisateurs";
}

    @PostMapping("/utilisateur/{id}/toggle")
    public String toggleUtilisateur(@PathVariable Long id, @RequestParam String role) {
        if (role.equals("CANDIDAT")) {
            Candidat candidat = candidatService.findById(id);
            if (candidat != null) {
                candidat.setActive(!candidat.isActive());
                candidatService.save(candidat);
            }
        } else if (role.equals("RECRUTEUR")) {
            Recruteur recruteur = recruteurService.findById(id);
            if (recruteur != null) {
                recruteur.setActive(!recruteur.isActive());
                recruteurService.save(recruteur);
            }
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/utilisateur/{id}/supprimer")
    public String supprimerUtilisateur(@PathVariable Long id, @RequestParam String role) {
        if (role.equals("CANDIDAT")) {
            candidatService.deleteById(id);
        } else if (role.equals("RECRUTEUR")) {
            recruteurService.deleteById(id);
        }
        return "redirect:/admin/utilisateurs";
    }

}
