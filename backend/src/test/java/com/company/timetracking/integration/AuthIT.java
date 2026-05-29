package com.company.timetracking.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/** End-to-end authentication and route-protection behavior. */
class AuthIT extends AbstractIntegrationTest {

    @Test
    void loginWithValidCredentialsReturnsBearerToken() {
        var entity = new HttpEntity<>(Map.of("username", "admin", "password", "admin"), jsonHeaders());

        var response = rest.exchange("/api/v1/auth/login", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var data = readTree(response.getBody()).path("data");
        assertThat(data.path("accessToken").asText()).isNotBlank();
        assertThat(data.path("tokenType").asText()).isEqualTo("Bearer");
        assertThat(data.path("expiresInMs").asLong()).isPositive();
    }

    @Test
    void loginWithWrongPasswordReturns401() {
        var entity = new HttpEntity<>(Map.of("username", "admin", "password", "wrong"), jsonHeaders());

        var response = rest.exchange("/api/v1/auth/login", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(readTree(response.getBody()).path("errors").get(0).path("code").asText())
                .isEqualTo("INVALID_CREDENTIALS");
    }

    @Test
    void loginWithBlankFieldsReturns400() {
        var entity = new HttpEntity<>(Map.of("username", "", "password", ""), jsonHeaders());

        var response = rest.exchange("/api/v1/auth/login", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(readTree(response.getBody()).path("success").asBoolean()).isFalse();
    }

    @Test
    void protectedRouteWithoutTokenReturns401() {
        var response = rest.getForEntity("/api/v1/collaborators", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void protectedRouteWithGarbageTokenReturns401() {
        var response = send(HttpMethod.GET, "/api/v1/collaborators", "not-a-real-token", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
