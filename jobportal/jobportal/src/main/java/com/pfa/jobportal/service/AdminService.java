package com.pfa.jobportal.service;

import com.pfa.jobportal.model.Admin;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    void createAdmin(Admin admin, MultipartFile imageFile);
    Admin getLatestAdmin();
}
