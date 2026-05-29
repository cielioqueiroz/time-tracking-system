package com.company.timetracking.domain.exceptions;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("EMAIL_ALREADY_EXISTS", "Já existe um colaborador com o e-mail: " + email);
    }
}
