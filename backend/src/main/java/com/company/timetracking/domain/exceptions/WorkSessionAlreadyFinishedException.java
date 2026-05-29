package com.company.timetracking.domain.exceptions;

public class WorkSessionAlreadyFinishedException extends DomainException {
    public WorkSessionAlreadyFinishedException(String id) {
        super("WORK_SESSION_ALREADY_FINISHED", "A jornada já foi encerrada: " + id);
    }
}
