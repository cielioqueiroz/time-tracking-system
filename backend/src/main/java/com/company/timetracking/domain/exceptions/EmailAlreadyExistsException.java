package com.company.timetracking.domain.exceptions;

/** Raised when creating/updating a collaborator with an email already in use. */
public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("EMAIL_ALREADY_EXISTS", "Já existe um colaborador com o e-mail: " + email);
    }
}
