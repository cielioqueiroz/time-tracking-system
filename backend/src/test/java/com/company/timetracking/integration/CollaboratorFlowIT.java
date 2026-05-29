package com.company.timetracking.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CollaboratorFlowIT extends AbstractIntegrationTest {

    private static final String COLLABORATORS = "/api/v1/collaborators";

    private String createCollaborator(String token, String name, String email) {
        var response = send(HttpMethod.POST, COLLABORATORS, token,
                Map.of("name", name, "email", email, "cargo", "Desenvolvedor"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return readTree(response.getBody()).path("data").path("id").asText();
    }

    @Test
    void fullLifecycle_create_start_finish_history_update_delete() {
        String token = adminToken();

        var createResp = send(HttpMethod.POST, COLLABORATORS, token,
                Map.of("name", "Ana Souza", "email", "ana@empresa.com", "cargo", "Analista"));
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var created = readTree(createResp.getBody()).path("data");
        String id = created.path("id").asText();
        assertThat(created.path("name").asText()).isEqualTo("Ana Souza");
        assertThat(created.path("cargo").asText()).isEqualTo("Analista");
        assertThat(created.path("status").asText()).isEqualTo("FORA_DA_JORNADA");

        var getResp = send(HttpMethod.GET, COLLABORATORS + "/" + id, token, null);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readTree(getResp.getBody()).path("data").path("email").asText())
                .isEqualTo("ana@empresa.com");

        var listResp = send(HttpMethod.GET, COLLABORATORS, token, null);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        var listData = readTree(listResp.getBody()).path("data");
        assertThat(listData.path("totalElements").asLong()).isEqualTo(1L);
        assertThat(listData.path("content").get(0).path("id").asText()).isEqualTo(id);

        var startResp = send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/start", token, null);
        assertThat(startResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(readTree(startResp.getBody()).path("data").path("status").asText())
                .isEqualTo("EM_ANDAMENTO");

        var afterStart = send(HttpMethod.GET, COLLABORATORS + "/" + id, token, null);
        assertThat(readTree(afterStart.getBody()).path("data").path("status").asText())
                .isEqualTo("TRABALHANDO");

        var finishResp = send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/finish", token, null);
        assertThat(finishResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        var finished = readTree(finishResp.getBody()).path("data");
        assertThat(finished.path("status").asText()).isEqualTo("FINALIZADA");
        assertThat(finished.path("totalMinutes").isNull()).isFalse();
        assertThat(finished.path("totalMinutes").asLong()).isGreaterThanOrEqualTo(0L);

        var afterFinish = send(HttpMethod.GET, COLLABORATORS + "/" + id, token, null);
        assertThat(readTree(afterFinish.getBody()).path("data").path("status").asText())
                .isEqualTo("FORA_DA_JORNADA");

        var historyResp = send(HttpMethod.GET, COLLABORATORS + "/" + id + "/work-sessions", token, null);
        assertThat(historyResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readTree(historyResp.getBody()).path("data").path("totalElements").asLong())
                .isEqualTo(1L);

        var updateResp = send(HttpMethod.PUT, COLLABORATORS + "/" + id, token,
                Map.of("name", "Ana Lima", "email", "ana.lima@empresa.com", "cargo", "Tech Lead"));
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        var updated = readTree(updateResp.getBody()).path("data");
        assertThat(updated.path("name").asText()).isEqualTo("Ana Lima");
        assertThat(updated.path("cargo").asText()).isEqualTo("Tech Lead");

        var deleteResp = send(HttpMethod.DELETE, COLLABORATORS + "/" + id, token, null);
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var goneResp = send(HttpMethod.GET, COLLABORATORS + "/" + id, token, null);
        assertThat(goneResp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void duplicateEmailReturns409() {
        String token = adminToken();
        createCollaborator(token, "Primeiro", "dup@empresa.com");

        var response = send(HttpMethod.POST, COLLABORATORS, token,
                Map.of("name", "Segundo", "email", "dup@empresa.com", "cargo", "Dev"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(readTree(response.getBody()).path("errors").get(0).path("code").asText())
                .isEqualTo("EMAIL_ALREADY_EXISTS");
    }

    @Test
    void startingSecondJourneyWhileActiveReturns409() {
        String token = adminToken();
        String id = createCollaborator(token, "Bob", "bob@empresa.com");
        send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/start", token, null);

        var response = send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/start", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void finishingWithoutActiveJourneyReturns404() {
        String token = adminToken();
        String id = createCollaborator(token, "Carol", "carol@empresa.com");

        var response = send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/finish", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createWithInvalidEmailReturns400WithFieldError() {
        String token = adminToken();

        var response = send(HttpMethod.POST, COLLABORATORS, token,
                Map.of("name", "Dan", "email", "not-an-email", "cargo", "Dev"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var firstError = readTree(response.getBody()).path("errors").get(0);
        assertThat(firstError.path("field").asText()).isEqualTo("email");
    }

    @Test
    void summaryReflectsFinishedSessions() {
        String token = adminToken();
        String id = createCollaborator(token, "Edu", "edu@empresa.com");
        send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/start", token, null);
        send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/finish", token, null);

        var response = send(HttpMethod.GET, COLLABORATORS + "/" + id + "/work-sessions/summary", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var data = readTree(response.getBody()).path("data");
        assertThat(data.path("totalSessions").asLong()).isEqualTo(1L);
        assertThat(data.path("finishedSessions").asLong()).isEqualTo(1L);
        assertThat(data.path("totalMinutes").asLong()).isGreaterThanOrEqualTo(0L);
    }

    @Test
    void exportReturnsCsvWithSessions() {
        String token = adminToken();
        String id = createCollaborator(token, "Fran", "fran@empresa.com");
        send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/start", token, null);
        send(HttpMethod.POST, COLLABORATORS + "/" + id + "/work-sessions/finish", token, null);

        var response = send(HttpMethod.GET, COLLABORATORS + "/" + id + "/work-sessions/export", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("text/csv");
        assertThat(response.getHeaders().getFirst("Content-Disposition")).contains("attachment");
        String body = response.getBody();
        assertThat(body).startsWith("id;status;inicio;fim;minutos");
        assertThat(body).contains("FINALIZADA");
    }

    @Test
    void exportForNonexistentCollaboratorReturns404() {
        String token = adminToken();

        var response = send(HttpMethod.GET,
                COLLABORATORS + "/11111111-1111-1111-1111-111111111111/work-sessions/export", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void summaryForNonexistentCollaboratorReturns404() {
        String token = adminToken();

        var response = send(HttpMethod.GET,
                COLLABORATORS + "/11111111-1111-1111-1111-111111111111/work-sessions/summary", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getNonexistentCollaboratorReturns404() {
        String token = adminToken();

        var response = send(HttpMethod.GET,
                COLLABORATORS + "/11111111-1111-1111-1111-111111111111", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void invalidUuidReturns400() {
        String token = adminToken();

        ResponseEntity<String> response = send(HttpMethod.GET, COLLABORATORS + "/not-a-uuid", token, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
