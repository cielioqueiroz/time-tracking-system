package com.company.timetracking.presentation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A single, machine-readable error item carried by {@link ApiResponse}.
 *
 * @param code    stable, machine-readable error code (e.g. {@code EMAIL_ALREADY_EXISTS})
 * @param message human-readable description
 * @param field   offending field for validation errors; {@code null} otherwise
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String code, String message, String field) {

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null);
    }

    public static ApiError ofField(String code, String message, String field) {
        return new ApiError(code, message, field);
    }
}
