package com.pfa.jobportal.repository;

import com.pfa.jobportal.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends GenericRepository<Admin, Long> {
}
