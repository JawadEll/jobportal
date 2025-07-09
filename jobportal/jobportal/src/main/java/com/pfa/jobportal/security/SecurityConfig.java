package com.pfa.jobportal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    /**
     * ✅ Encodeur de mots de passe (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ✅ Configuration de la sécurité HTTP
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Désactivation de CSRF pour permettre les requêtes JS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/admin/create", "/uploads/**",
                                "/forgot-password", "/reset-password", "/send-reset-email").permitAll()
                        .requestMatchers(HttpMethod.GET, "/recruteur/chat/**").hasRole("RECRUTEUR")
                        .requestMatchers(HttpMethod.POST, "/recruteur/chat/**").hasRole("RECRUTEUR") // ✅ cette ligne est CRUCIALE
                        .requestMatchers(HttpMethod.POST, "/recruteur/api/messages/**").hasRole("RECRUTEUR")
                        .requestMatchers(HttpMethod.GET, "/recruteur/api/messages/**").hasAnyRole("RECRUTEUR", "CANDIDAT")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/recruteur/**").hasRole("RECRUTEUR")
                        .requestMatchers("/candidat/**").hasRole("CANDIDAT")
                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * ✅ Gestionnaire d'authentification (utilisé en interne par Spring Security)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
