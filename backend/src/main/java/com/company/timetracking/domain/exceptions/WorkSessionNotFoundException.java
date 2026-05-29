package com.company.timetracking.domain.exceptions;

public class WorkSessionNotFoundException extends DomainException {
    public WorkSessionNotFoundException(String detail) {
        super("WORK_SESSION_NOT_FOUND", "Jornada não encontrada: " + detail);
    }
}
