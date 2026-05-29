package com.company.timetracking.domain.exceptions;

/** Raised when attempting to finish a session that is already finished. */
public class WorkSessionAlreadyFinishedException extends DomainException {
    public WorkSessionAlreadyFinishedException(String id) {
        super("WORK_SESSION_ALREADY_FINISHED", "A jornada já foi encerrada: " + id);
    }
}
