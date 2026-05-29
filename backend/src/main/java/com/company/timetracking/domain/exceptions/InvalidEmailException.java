package com.company.timetracking.domain.exceptions;

/** Raised when an email value is missing or malformed. */
public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String value) {
        super("INVALID_EMAIL", "E-mail inválido: " + value);
    }
}
