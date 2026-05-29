package com.company.timetracking.domain.exceptions;

/**
 * Base type for all domain/business-rule violations.
 *
 * <p>Carries a stable, machine-readable {@code code} so the presentation layer
 * can map it to an HTTP status and an {@code ApiError} without string-matching
 * on messages.
 */
public abstract class DomainException extends RuntimeException {

    private final String code;

    protected DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}
