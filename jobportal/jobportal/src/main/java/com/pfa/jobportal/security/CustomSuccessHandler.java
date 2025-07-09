package com.pfa.jobportal.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String role = authentication.getAuthorities().toString();

        if (role.contains("ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (role.contains("RECRUTEUR")) {
            response.sendRedirect("/recruteur/dashboard");
        } else if (role.contains("CANDIDAT")) {
            response.sendRedirect("/candidat/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}
