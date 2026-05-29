package com.company.timetracking.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed security configuration bound from {@code application.security.*}.
 */
@ConfigurationProperties(prefix = "application.security")
public record SecurityProperties(Jwt jwt, Admin admin) {

    public record Jwt(String secret, long expiration) {}

    /** Single bootstrap admin user for the simple JWT auth. */
    public record Admin(String username, String password) {}
}
