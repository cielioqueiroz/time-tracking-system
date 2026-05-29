package com.company.timetracking.application.queries;

import java.time.Instant;

/**
 * Read-intent input for a collaborator's work-hours summary.
 * {@code from}/{@code to} are optional period bounds (nullable).
 */
public record WorkSummaryQuery(String collaboratorId, Instant from, Instant to) {}
