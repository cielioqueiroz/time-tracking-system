package com.company.timetracking.domain.exceptions;

/** Raised when collaborator data violates an invariant (e.g. blank name). */
public class InvalidCollaboratorDataException extends DomainException {
    public InvalidCollaboratorDataException(String message) {
        super("INVALID_COLLABORATOR_DATA", message);
    }
}
