package com.company.timetracking.domain.valueobjects;

import java.time.Duration;
import java.time.Instant;

/**
 * Immutable time span of a work session. Encapsulates the "calculate worked
 * time automatically" rule: an open period has no end and no duration; closing
 * it derives the total minutes.
 */
public record WorkPeriod(Instant startedAt, Instant endedAt) {

    public WorkPeriod {
        if (startedAt == null) {
            throw new IllegalArgumentException("startedAt é obrigatório");
        }
        if (endedAt != null && endedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("endedAt não pode ser anterior a startedAt");
        }
    }

    /** Opens a new, ongoing period at the given instant. */
    public static WorkPeriod startingAt(Instant startedAt) {
        return new WorkPeriod(startedAt, null);
    }

    public boolean isOpen() {
        return endedAt == null;
    }

    /** Returns a closed copy of this period ending at {@code end}. */
    public WorkPeriod closeAt(Instant end) {
        return new WorkPeriod(startedAt, end);
    }

    /** Total worked minutes, or {@code null} while the period is still open. */
    public Long totalMinutes() {
        return isOpen() ? null : Duration.between(startedAt, endedAt).toMinutes();
    }
}
