package com.company.timetracking.domain.exceptions;

/**
 * Raised when there is no work session to operate on — e.g. trying to finish a
 * journey that does not exist / is not open.
 */
public class WorkSessionNotFoundException extends DomainException {
    public WorkSessionNotFoundException(String detail) {
        super("WORK_SESSION_NOT_FOUND", "Jornada não encontrada: " + detail);
    }
}
