package com.company.timetracking.presentation.responses;

public record WorkSummaryResponse(
        String collaboratorId,
        long totalSessions,
        long finishedSessions,
        long totalMinutes
) {}
