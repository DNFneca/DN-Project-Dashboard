package com.dn.projectdashboard.Token;

import com.dn.projectdashboard.DTO.AuthResponse;
import com.dn.projectdashboard.Service.AuthService;
import com.dn.projectdashboard.Service.SessionService;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Controller
public class TokenController {

    private TokenRepository tokenRepository;
    private AuthService authService;
    private SessionService sessionService;

    @QueryMapping
    public AuthResponse validateToken(DataFetchingEnvironment env, HttpServletResponse response) {
        String token = env.getGraphQlContext().get("token");
        Boolean isValidSession = sessionService.isValidSession(token);
        System.out.println("isValidSession: " + isValidSession);
        if (isValidSession == null || isValidSession) return new AuthResponse();

        var cookie = new Cookie("AUTH_TOKEN_DNPD", sessionService.generateSessionFromOldToken(token));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(sessionService.generateSessionFromOldToken(token));
        authResponse.setMessage("New token");
        tokenRepository.removeByTokenEquals(token);
        return authResponse;
    }

    @QueryMapping
    public Boolean tokenValidity(DataFetchingEnvironment env) {
        String token = env.getGraphQlContext().get("token");
        System.out.println("Token: " + token);
        System.out.println("Token: " + env.getGraphQlContext().get("userId"));
        return sessionService.isValidSession(token);
    }
}
