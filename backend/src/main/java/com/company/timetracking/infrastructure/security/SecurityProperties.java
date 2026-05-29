package com.company.timetracking.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security")
public record SecurityProperties(Jwt jwt, Admin admin) {

    public record Jwt(String secret, long expiration) {}

    public record Admin(String username, String password) {}
}
