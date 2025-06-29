package com.dn.projectdashboard.Service;

import com.dn.projectdashboard.Token.Token;
import com.dn.projectdashboard.Token.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private final long sessionTimeout = 3600000; // 1 hour
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

    public boolean isValidSession(String token) {
        try {
            System.out.println(1);
            System.out.println(tokenRepository.existsByTokenEquals(token));
            if (!tokenRepository.existsByTokenEquals(token)) {
                return false;
            }

            System.out.println(2);
            System.out.println(Arrays.toString(tokenRepository.findByTokenEquals(token).getBytes()));


            Claims claims = Jwts.parser()
                    .setSigningKey(tokenRepository.findByTokenEquals(token).getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println(3);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            tokenRepository.removeByTokenEquals(token);
            return false;
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
        if (isValidSession(token)) {
            String session = createSession(getUserIdFromToken(token), getUsernameFromToken(token));
            invalidateSession(token);
            return session;
        } else {
            return null;
        }
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