package com.example.stellarinvestment.repository;

import com.example.stellarinvestment.model.AuthenticationType;
import com.example.stellarinvestment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByVerificationCode(String verificationCode);
    @Query("UPDATE User c SET c.authenticationType = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateAuthenticationType(Integer customerId, AuthenticationType type);

    User findByResetPasswordToken(String token);
}