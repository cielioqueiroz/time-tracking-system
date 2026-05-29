package com.company.timetracking.application.queries;

/** Read-intent input for a collaborator's work-session history. */
public record WorkSessionHistoryQuery(String collaboratorId, int page, int size) {}
