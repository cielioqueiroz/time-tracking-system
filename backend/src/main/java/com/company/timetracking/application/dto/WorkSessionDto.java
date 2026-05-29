package com.company.timetracking.application.dto;

import com.company.timetracking.domain.enums.WorkSessionStatus;

import java.time.Instant;

public record WorkSessionDto(
        String id,
        String collaboratorId,
        WorkSessionStatus status,
        Instant startedAt,
        Instant endedAt,
        Long totalMinutes
) {}
