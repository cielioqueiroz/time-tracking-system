package com.company.timetracking.presentation.responses;

/** Outbound view model for a collaborator's work-hours summary. */
public record WorkSummaryResponse(
        String collaboratorId,
        long totalSessions,
        long finishedSessions,
        long totalMinutes
) {}
