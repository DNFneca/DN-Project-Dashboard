package com.dn.projectdashboard.Token;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    long countTokensByTokenEquals(String token);

    Token findByTokenEquals(String token);


    boolean existsTokenByToken(String token);

    @Transactional
    void removeByTokenEquals(String token);
}
