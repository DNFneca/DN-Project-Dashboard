package com.dn.projectdashboard.Service;

import com.dn.projectdashboard.Person.Person;
import com.dn.projectdashboard.Person.PersonRepository;
import com.dn.projectdashboard.Token.Token;
import com.dn.projectdashboard.Token.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private PersonRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SessionService sessionService;
    private TokenRepository tokenRepository;

    public String login(String username, String password) {
        Person user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        Token token = new Token();
        token.setToken(sessionService.createSession(user.getId(), user.getUsername()));
        token.setBytes(SessionService.key.getEncoded());

        return token.getToken();
    }

    public Person register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Person user = new Person();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public void logout(String token) {
        sessionService.invalidateSession(token);
    }
}
