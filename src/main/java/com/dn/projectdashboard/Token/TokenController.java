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
    private HttpServletResponse response;

    @QueryMapping
    public AuthResponse validateToken(DataFetchingEnvironment env) {
        String refreshToken = env.getGraphQlContext().get("refreshToken");
        Boolean isValidSession = sessionService.isValidSession(refreshToken);
        System.out.println("isValidSession: " + isValidSession);
        if (isValidSession == null || !isValidSession) return new AuthResponse();

        AuthResponse authResponse = new AuthResponse();
        String session = sessionService.generateSessionFromOldToken(refreshToken);
        System.out.println(session);
        authResponse.setToken(session);
        authResponse.setMessage("New refreshToken");
        tokenRepository.removeByTokenEquals(refreshToken);
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
