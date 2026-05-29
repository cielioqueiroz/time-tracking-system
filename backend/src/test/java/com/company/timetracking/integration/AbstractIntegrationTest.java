package com.company.timetracking.integration;

import com.company.timetracking.infrastructure.persistence.jpa.repositories.CollaboratorJpaRepository;
import com.company.timetracking.infrastructure.persistence.jpa.repositories.WorkSessionJpaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Base for HTTP integration tests. Boots the whole Spring context against a real
 * PostgreSQL (Testcontainers) on a random port, exercising the full wiring:
 * controllers → use cases → JPA adapters → Flyway-migrated schema, plus the JWT
 * filter and {@code GlobalExceptionHandler}.
 *
 * <p>The container is managed by the Testcontainers JUnit extension. With
 * {@code disabledWithoutDocker = true}, these tests are <em>skipped</em> (never
 * failed) when no usable Docker environment is found — so {@code mvn verify}
 * stays green on any machine, and the suite still runs in CI where Docker is
 * available. Each test starts from a clean database.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
abstract class AbstractIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    protected TestRestTemplate rest;
    @Autowired
    protected ObjectMapper json;
    @Autowired
    private CollaboratorJpaRepository collaboratorRepo;
    @Autowired
    private WorkSessionJpaRepository workSessionRepo;

    @BeforeEach
    void cleanDatabase() {
        // Sessions first — they reference collaborators (FK).
        workSessionRepo.deleteAll();
        collaboratorRepo.deleteAll();
    }

    // ───────────────────────── helpers ─────────────────────────

    protected String loginAndGetToken(String username, String password) {
        var entity = new HttpEntity<>(Map.of("username", username, "password", password), jsonHeaders());
        var response = rest.exchange("/api/v1/auth/login", HttpMethod.POST, entity, String.class);
        return readTree(response.getBody()).path("data").path("accessToken").asText();
    }

    protected String adminToken() {
        return loginAndGetToken("admin", "admin");
    }

    protected ResponseEntity<String> send(HttpMethod method, String path, String token, Object body) {
        var entity = new HttpEntity<>(body, authHeaders(token));
        return rest.exchange(path, method, entity, String.class);
    }

    protected HttpHeaders jsonHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected HttpHeaders authHeaders(String token) {
        var headers = jsonHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    protected JsonNode readTree(String body) {
        try {
            return json.readTree(body);
        } catch (Exception e) {
            throw new IllegalStateException("Resposta JSON inválida: " + body, e);
        }
    }
}
