package com.company.timetracking.presentation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Standardized response envelope returned by every endpoint.
 *
 * <p>Keeps the wire format predictable for clients: a successful call always
 * carries {@code data} and no {@code errors}; a failed call carries
 * {@code errors} and no {@code data}.
 *
 * @param <T> payload type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        List<ApiError> errors,
        Instant timestamp
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> failure(List<ApiError> errors) {
        return new ApiResponse<>(false, null, errors, Instant.now());
    }

    public static <T> ApiResponse<T> failure(ApiError error) {
        return failure(List.of(error));
    }
}
