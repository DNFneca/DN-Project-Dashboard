package com.dn.projectdashboard.Service;

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


    public String createSession(Integer userId, String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + sessionTimeout))
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
                    .parseClaimsJws(token)
                    .getBody();

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
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Integer.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public String generateSessionFromOldToken(String token) {
        if (isValidSession(token)) return null;
        String session = createSession(getUserIdFromToken(token), getUsernameFromToken(token));
        invalidateSession(token);
        return session;
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