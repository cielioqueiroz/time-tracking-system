package com.company.timetracking.application.queries;

/** Read-intent input to list collaborators with pagination. */
public record ListCollaboratorsQuery(int page, int size) {}
