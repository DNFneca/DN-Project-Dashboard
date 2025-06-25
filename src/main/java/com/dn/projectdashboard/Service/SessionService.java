package com.dn.projectdashboard.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Map<String, Integer> activeSessions = new ConcurrentHashMap<>();
    private final long sessionTimeout = 3600000; // 1 hour

    public String createSession(Integer userId, String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + sessionTimeout))
                .signWith(key)
                .compact();

        activeSessions.put(token, userId);
        return token;
    }

    public boolean isValidSession(String token) {
        try {
            if (!activeSessions.containsKey(token)) {
                return false;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            activeSessions.remove(token);
            return false;
        }
    }

    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("userId", Integer.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public void invalidateSession(String token) {
        activeSessions.remove(token);
    }
}