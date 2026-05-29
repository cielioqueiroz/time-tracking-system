package com.company.timetracking.domain.exceptions;

/** Raised when starting a session while the collaborator already has one open. */
public class ActiveWorkSessionExistsException extends DomainException {
    public ActiveWorkSessionExistsException(String collaboratorId) {
        super("ACTIVE_WORK_SESSION_EXISTS",
                "O colaborador já possui uma jornada em andamento: " + collaboratorId);
    }
}
