package com.company.timetracking.domain.enums;

/**
 * Current working state of a collaborator, derived automatically from their
 * work sessions.
 */
public enum CollaboratorStatus {
    /** Has an open work session in progress. */
    TRABALHANDO,
    /** Not currently in a work session. */
    FORA_DA_JORNADA
}
