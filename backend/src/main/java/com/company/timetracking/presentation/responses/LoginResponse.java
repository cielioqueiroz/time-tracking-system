package com.company.timetracking.presentation.responses;

/** Outbound payload returned after a successful authentication. */
public record LoginResponse(String accessToken, String tokenType, long expiresInMs) {
    public static LoginResponse bearer(String token, long expiresInMs) {
        return new LoginResponse(token, "Bearer", expiresInMs);
    }
}
