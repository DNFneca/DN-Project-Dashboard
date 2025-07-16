package com.dn.projectdashboard.Service;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Person.PersonRepository;
import com.dn.projectdashboard.Token.Token;
import com.dn.projectdashboard.Token.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@AllArgsConstructor
public class SessionService {
    public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long accessTokenTimeout = 3600000; // 1 hour
    public static final long refreshTokenTimeout = 86400000; // 1 day
    private TokenRepository tokenRepository;
    private PersonRepository personRepository;
    private HttpServletResponse response;

    public String generateAccessToken(Person user) {
        return generateAccessToken(user.getId(), user.getUsername());
    }

    public String generateAccessToken(Integer userId, String username) {
        String token = Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenTimeout))
                .signWith(key)
                .compact();

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setBytes(key.getEncoded());

        tokenRepository.save(tokenEntity);
        return token;
    }


    public String createSession(Integer userId, String username) {
        String token = Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenTimeout))
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
                return false;
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

    public String generateSessionFromOldToken(String oldRefreshToken) {
        if (!isValidSession(oldRefreshToken)) return null;
        String session = createRefreshToken(getUserIdFromToken(oldRefreshToken), getUsernameFromToken(oldRefreshToken));
        var cookie = new Cookie("DNPD_AUTH_TOKEN", session);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) SessionService.refreshTokenTimeout);
        response.addCookie(cookie);
        createSession(getUserIdFromToken(oldRefreshToken), getUsernameFromToken(oldRefreshToken));
        invalidateSession(oldRefreshToken);
        return session;
    }

    public String generateRefreshToken(Integer userId, String username) {
        return createRefreshToken(userId, username);
    }

    private String createRefreshToken(Integer userId, String username) {
        String refreshToken = Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenTimeout))
                .issuer("dnpd")
                .signWith(key)
                .compact();

        Token tokenEntity = new Token();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setBytes(key.getEncoded());
        return tokenRepository.save(tokenEntity).getToken();
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