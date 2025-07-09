package com.pfa.jobportal.model.recupmtp;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public boolean isExpired() {
        return expiryDate.before(new Date());
    }
}
