package com.pfa.jobportal.controller;

import com.pfa.jobportal.service.recpmpt.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/password") // Ajout d'un préfixe pour éviter les conflits
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/forgot")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot")
    public String processForgotPassword(@RequestParam("email") String email, HttpServletRequest request, Model model) {
        String appUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
        passwordResetService.sendResetLink(email, appUrl, model);
        return "forgot-password";
    }

    @GetMapping("/reset")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!passwordResetService.isTokenValid(token)) {
            model.addAttribute("message", "❌ Lien invalide ou expiré.");
            return "forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        return passwordResetService.resetPassword(token, newPassword, confirmPassword, model);
    }
}
