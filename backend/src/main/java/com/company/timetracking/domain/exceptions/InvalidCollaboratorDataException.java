package com.company.timetracking.domain.exceptions;

public class InvalidCollaboratorDataException extends DomainException {
    public InvalidCollaboratorDataException(String message) {
        super("INVALID_COLLABORATOR_DATA", message);
    }
}
