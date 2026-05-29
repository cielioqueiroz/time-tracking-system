package com.company.timetracking.domain.valueobjects;

import java.time.Duration;
import java.time.Instant;

public record WorkPeriod(Instant startedAt, Instant endedAt) {

    public WorkPeriod {
        if (startedAt == null) {
            throw new IllegalArgumentException("startedAt é obrigatório");
        }
        if (endedAt != null && endedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("endedAt não pode ser anterior a startedAt");
        }
    }

    public static WorkPeriod startingAt(Instant startedAt) {
        return new WorkPeriod(startedAt, null);
    }

    public boolean isOpen() {
        return endedAt == null;
    }

    public WorkPeriod closeAt(Instant end) {
        return new WorkPeriod(startedAt, end);
    }

    public Long totalMinutes() {
        return isOpen() ? null : Duration.between(startedAt, endedAt).toMinutes();
    }
}
