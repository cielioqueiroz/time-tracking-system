package com.company.timetracking.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkPeriodTest {

    private final Instant start = Instant.parse("2026-05-29T08:00:00Z");

    @Test
    void openPeriodHasNoDuration() {
        var period = WorkPeriod.startingAt(start);
        assertThat(period.isOpen()).isTrue();
        assertThat(period.totalMinutes()).isNull();
    }

    @Test
    void closingComputesDurationInMinutes() {
        var period = WorkPeriod.startingAt(start).closeAt(start.plus(90, ChronoUnit.MINUTES));
        assertThat(period.isOpen()).isFalse();
        assertThat(period.totalMinutes()).isEqualTo(90);
    }

    @Test
    void rejectsEndBeforeStart() {
        assertThatThrownBy(() -> WorkPeriod.startingAt(start).closeAt(start.minusSeconds(60)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
