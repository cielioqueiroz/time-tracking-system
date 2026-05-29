package com.company.timetracking.infrastructure.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final SecurityProperties properties;
    private final JwtService jwtService;

    public AuthenticationService(SecurityProperties properties, JwtService jwtService) {
        this.properties = properties;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {
        var admin = properties.admin();
        boolean valid = admin.username().equals(username)
                && admin.password().equals(password);
        if (!valid) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }
        return jwtService.generateToken(username);
    }

    public long expiresInMs() {
        return jwtService.expirationMs();
    }
}
