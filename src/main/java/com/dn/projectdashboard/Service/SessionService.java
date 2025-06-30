package com.dn.projectdashboard.Service;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Person.PersonRepository;
import com.dn.projectdashboard.Token.Token;
import com.dn.projectdashboard.Token.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class SessionService {
    public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long sessionTimeout = 3600000; // 1 hour
    private TokenRepository tokenRepository;
    private PersonRepository personRepository;

    public String generateAccessToken(Person user) {
        return generateAccessToken(user.getId(), user.getUsername());
    }

    public String generateAccessToken(Integer userId) {
        personRepository.findById(userId).orElse(null);
        return null;
    }

    public String generateAccessToken(Integer userId, String username) {

    }
    public String createSession(Integer userId, String username) {
        String token = Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + sessionTimeout))
                .signWith(key)
                .compact();

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setBytes(key.getEncoded());

        tokenRepository.save(tokenEntity);
        return token;
    }

    public Boolean isValidSession(String token) {
        try {
            if (!tokenRepository.existsByTokenEquals(token)) {
                return null;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(tokenRepository.findByTokenEquals(token).getBytes())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                return false;
            }
            return null;
        }
    }
    public Boolean isValidRefreshToken(String token) {
        try {
            if (!tokenRepository.existsByTokenEquals(token)) {
                return null;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(tokenRepository.findByTokenEquals(token).getBytes())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                return false;
            }
            return null;
        }
    }


    public Integer getUserIdFromToken(String token) {
        try {
            System.out.println(tokenRepository.findByTokenEquals(token).getToken());
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenRepository.findByTokenEquals(token).getBytes())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("userId", Integer.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public String generateSessionFromOldToken(String token) {
        if (isValidSession(token)) return null;
        String session = createRefreshToken(getUserIdFromToken(token), getUsernameFromToken(token));
        invalidateSession(token);
        return session;
    }

    public String generateRefreshToken(String token) {
        return createRefreshToken(token);
    }

    private String createRefreshToken(String token) {
        String refreshToken = Jwts.builder()
                .claim("token", token)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + sessionTimeout))
                .issuer("dnpd")
                .signWith(key)
                .compact();

        Token tokenEntity = new Token();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setBytes(key.getEncoded());
        return refreshToken;
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenRepository.findByTokenEquals(token).getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public void invalidateSession(String token) {
        tokenRepository.removeByTokenEquals(token);
    }
}