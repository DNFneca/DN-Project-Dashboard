package com.dn.projectdashboard.Token;

import com.dn.projectdashboard.DTO.AuthResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    long countTokensByTokenEquals(String token);

    AuthResponse findByTokenEquals(String token);
}
