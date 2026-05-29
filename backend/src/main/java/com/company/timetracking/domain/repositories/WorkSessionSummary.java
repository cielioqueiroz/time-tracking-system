package com.company.timetracking.domain.repositories;

/**
 * Aggregated work-session statistics for a collaborator over a period,
 * computed by the persistence layer (not in memory).
 *
 * @param totalSessions    all sessions in the period
 * @param finishedSessions sessions already closed ({@code FINALIZADA})
 * @param totalMinutes     sum of worked minutes across finished sessions
 */
public record WorkSessionSummary(long totalSessions, long finishedSessions, long totalMinutes) {}
