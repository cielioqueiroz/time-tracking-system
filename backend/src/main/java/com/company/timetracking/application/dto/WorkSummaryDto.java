package com.company.timetracking.application.dto;

public record WorkSummaryDto(
        String collaboratorId,
        long totalSessions,
        long finishedSessions,
        long totalMinutes
) {}
