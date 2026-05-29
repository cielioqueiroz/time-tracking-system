package com.company.timetracking.domain.entities;

import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.domain.exceptions.WorkSessionAlreadyFinishedException;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkSessionTest {

    private final CollaboratorId collaboratorId = CollaboratorId.generate();
    private final Instant start = Instant.parse("2026-05-29T08:00:00Z");

    @Test
    void startsInProgressAndOpen() {
        var session = WorkSession.start(collaboratorId, start);
        assertThat(session.status()).isEqualTo(WorkSessionStatus.EM_ANDAMENTO);
        assertThat(session.isOpen()).isTrue();
        assertThat(session.totalMinutes()).isNull();
    }

    @Test
    void finishingComputesMinutesAndFlipsStatus() {
        var session = WorkSession.start(collaboratorId, start);
        session.finish(start.plus(120, ChronoUnit.MINUTES));

        assertThat(session.status()).isEqualTo(WorkSessionStatus.FINALIZADA);
        assertThat(session.totalMinutes()).isEqualTo(120);
        assertThat(session.endedAt()).isNotNull();
    }

    @Test
    void cannotFinishTwice() {
        var session = WorkSession.start(collaboratorId, start);
        session.finish(start.plus(60, ChronoUnit.MINUTES));

        assertThatThrownBy(() -> session.finish(start.plus(90, ChronoUnit.MINUTES)))
                .isInstanceOf(WorkSessionAlreadyFinishedException.class);
    }
}
