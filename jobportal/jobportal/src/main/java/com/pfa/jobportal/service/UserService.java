// === FICHIER : service/UserService.java ===
package com.pfa.jobportal.service;

import com.pfa.jobportal.model.User;

public interface UserService {
    User register(User user);
    User findByEmail(String email);
    long count();  // interface

}
