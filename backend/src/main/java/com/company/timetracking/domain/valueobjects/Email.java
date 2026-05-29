package com.company.timetracking.domain.valueobjects;

import com.company.timetracking.domain.exceptions.InvalidEmailException;

import java.util.regex.Pattern;

/**
 * Immutable, self-validating email value object. Stored normalized
 * (trimmed + lower-cased) so equality and uniqueness are case-insensitive.
 */
public record Email(String value) {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("vazio");
        }
        value = value.trim().toLowerCase();
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException(value);
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
