package com.dn.projectdashboard.Token;

import com.dn.projectdashboard.DTO.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class TokenController {

    private TokenRepository tokenRepository;

    @MutationMapping
    public AuthResponse validateToken(@Argument String token) {
        if (tokenRepository.countTokensByTokenEquals(token) > 0) {
            return tokenRepository.findByTokenEquals(token);
        } else {

        }
    }
}
