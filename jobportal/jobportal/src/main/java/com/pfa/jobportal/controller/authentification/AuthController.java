package com.pfa.jobportal.controller.authentification;

import com.pfa.jobportal.model.*;
import com.pfa.jobportal.model.enumm.ERole;
import com.pfa.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", ERole.values());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User userForm) {
        User user;
        switch (userForm.getRole()) {
            case ADMIN -> user = new Admin();
            case RECRUTEUR -> user = new Recruteur();
            case CANDIDAT -> user = new Candidat();
            default -> throw new IllegalArgumentException("Rôle non reconnu : " + userForm.getRole());
        }

        user.setNom(userForm.getNom());
        user.setEmail(userForm.getEmail());
        user.setMotDePasse(userForm.getMotDePasse());
        user.setRole(userForm.getRole());

        User savedUser = userService.register(user);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getMotDePasse(), null)
        );

        if (savedUser instanceof Candidat) {
            return "redirect:/candidat/profil";
        } else if (savedUser instanceof Recruteur) {
            return "redirect:/recruteur/dashboard";
        } else if (savedUser instanceof Admin) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }
}
