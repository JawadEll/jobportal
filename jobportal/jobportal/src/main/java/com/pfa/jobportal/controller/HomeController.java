package com.pfa.jobportal.controller;

import com.pfa.jobportal.model.Admin;
import com.pfa.jobportal.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String homePage(Model model, @RequestParam(name = "logout", required = false) String logout) {
        boolean adminExists = adminService.getLatestAdmin() != null;
        model.addAttribute("adminExists", adminExists);

        if ("success".equals(logout) && adminExists) {
            Admin admin = adminService.getLatestAdmin();
            model.addAttribute("message", "L’administrateur " + admin.getNom() + " a été déconnecté avec succès.");
        }

        return "home";
    }
}
