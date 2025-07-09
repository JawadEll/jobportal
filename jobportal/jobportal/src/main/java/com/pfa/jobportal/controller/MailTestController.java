package com.pfa.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailTestController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/test")
    public String testEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("jawadmatos8@gmail.com");
        msg.setSubject("Test Mail JobPortal");
        msg.setText("Ceci est un test d'envoi d'email depuis JobPortal.");

        mailSender.send(msg);
        return "✅ Email envoyé avec succès";
    }
}
