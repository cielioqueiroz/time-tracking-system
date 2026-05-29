package com.company.timetracking.domain.exceptions;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String value) {
        super("INVALID_EMAIL", "E-mail inválido: " + value);
    }
}
