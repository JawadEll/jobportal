package com.pfa.jobportal.service.recpmpt;

import com.pfa.jobportal.model.User;
import com.pfa.jobportal.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> resetTokenMap = new HashMap<>();

    public void sendResetLink(String email, String appUrl, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            String token = UUID.randomUUID().toString();
            resetTokenMap.put(token, email);

            String resetLink = appUrl + "/reset-password?token=" + token;

            try {
                sendEmail(email, resetLink);
                model.addAttribute("message", "📩 Un lien a été envoyé à votre email.");
            } catch (MessagingException e) {
                model.addAttribute("message", "❌ Erreur lors de l'envoi du mail.");
            }
        } else {
            model.addAttribute("message", "❌ Aucun compte avec cet email.");
        }
    }

    private void sendEmail(String to, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("Réinitialisation de votre mot de passe");
        helper.setText("<p>Bonjour,</p>" +
                "<p>Cliquez sur le lien ci-dessous pour réinitialiser votre mot de passe :</p>" +
                "<p><a href=\"" + resetLink + "\">Réinitialiser le mot de passe</a></p>" +
                "<p>Si vous n'avez pas demandé cela, ignorez ce message.</p>", true);
        mailSender.send(message);
    }

    public boolean isTokenValid(String token) {
        return resetTokenMap.containsKey(token);
    }

    @Transactional
    public String resetPassword(String token, String newPassword, String confirmPassword, Model model) {
        if (!resetTokenMap.containsKey(token)) {
            model.addAttribute("message", "❌ Lien expiré ou invalide.");
            return "forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("message", "❌ Les mots de passe ne correspondent pas.");
            return "reset-password";
        }

        String email = resetTokenMap.get(token);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setMotDePasse(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            resetTokenMap.remove(token);
            return "redirect:/login?resetSuccess";
        }

        model.addAttribute("message", "❌ Erreur lors de la réinitialisation.");
        return "reset-password";
    }
}
