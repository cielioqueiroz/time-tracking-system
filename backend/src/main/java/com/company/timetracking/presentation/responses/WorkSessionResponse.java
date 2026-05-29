package com.company.timetracking.presentation.responses;

import com.company.timetracking.domain.enums.WorkSessionStatus;

import java.time.Instant;

/** Outbound view model for a work session. */
public record WorkSessionResponse(
        String id,
        String collaboratorId,
        WorkSessionStatus status,
        Instant startedAt,
        Instant endedAt,
        Long totalMinutes
) {}
