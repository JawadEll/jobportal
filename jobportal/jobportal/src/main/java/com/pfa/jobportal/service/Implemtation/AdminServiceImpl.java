package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.Admin;
import com.pfa.jobportal.model.enumm.ERole;
import com.pfa.jobportal.repository.AdminRepository;
import com.pfa.jobportal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads";

    @Override
    public void createAdmin(Admin admin, MultipartFile imageFile) {
        if (adminRepository.count() > 0) {
            throw new IllegalStateException("Un administrateur existe déjà.");
        }

        try {
            // ✅ Créer le dossier s'il n'existe pas
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // ✅ Gérer l'image (upload ou défaut)
            String fileName;
            if (imageFile == null || imageFile.isEmpty()) {
                fileName = "admin1.png"; // image par défaut
            } else {
                fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // ✅ Encoder le mot de passe
            String motDePasseEncode = passwordEncoder.encode(admin.getMotDePasse());

            // ✅ Enregistrer l'admin
            admin.setImage(fileName);
            admin.setMotDePasse(motDePasseEncode);
            admin.setRole(ERole.ADMIN);
            adminRepository.save(admin);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du traitement de l'image", e);
        }
    }

    @Override
    public Admin getLatestAdmin() {
        return adminRepository.findAll().stream().findFirst().orElse(null);
    }
}
