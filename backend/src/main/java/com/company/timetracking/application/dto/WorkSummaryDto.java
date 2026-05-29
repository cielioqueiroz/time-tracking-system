package com.company.timetracking.application.dto;

/** Application-level read model for a collaborator's work-hours summary. */
public record WorkSummaryDto(
        String collaboratorId,
        long totalSessions,
        long finishedSessions,
        long totalMinutes
) {}
