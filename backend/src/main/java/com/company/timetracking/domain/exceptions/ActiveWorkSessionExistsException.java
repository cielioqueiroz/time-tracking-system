package com.company.timetracking.domain.exceptions;

public class ActiveWorkSessionExistsException extends DomainException {
    public ActiveWorkSessionExistsException(String collaboratorId) {
        super("ACTIVE_WORK_SESSION_EXISTS",
                "O colaborador já possui uma jornada em andamento: " + collaboratorId);
    }
}
