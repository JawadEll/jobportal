package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.User;
import com.pfa.jobportal.model.enumm.ERole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findTopByRoleOrderByIdDesc(ERole role);

}
