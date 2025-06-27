package com.dn.projectdashboard.Token;

import com.dn.projectdashboard.DTO.AuthResponse;
import com.dn.projectdashboard.Service.AuthService;
import com.dn.projectdashboard.Service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Controller
public class TokenController {

    private TokenRepository tokenRepository;
    private AuthService authService;
    private SessionService sessionService;

    @MutationMapping
    public AuthResponse validateToken(@Argument String token) {
        System.out.println("Token: " + token);
        if (sessionService.isValidSession(token)) {
            return null;
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(sessionService.generateSessionFromOldToken(token));
        authResponse.setMessage("New token");
        return authResponse;
    }
}
