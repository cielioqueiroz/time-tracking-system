package com.company.timetracking.presentation.handlers;

import com.company.timetracking.presentation.responses.ApiError;
import com.company.timetracking.presentation.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized translation of exceptions into the standardized
 * {@link ApiResponse} envelope.
 *
 * <p>ETAPA 1 ships only the catch-all fallback. Domain-specific handlers
 * (validation errors, not-found, business-rule violations) are added in
 * ETAPA 3 once the corresponding exceptions exist.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        ApiError error = ApiError.of("INTERNAL_ERROR", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(error));
    }
}
