package com.pfa.jobportal.service.Implemtation;

import com.pfa.jobportal.model.User;
import com.pfa.jobportal.repository.UserRepository;
import com.pfa.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public User register(User user) {
        // ✅ Encodage du mot de passe avant sauvegarde
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));

        // ✅ Sauvegarde de l'utilisateur dans la base de données
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
