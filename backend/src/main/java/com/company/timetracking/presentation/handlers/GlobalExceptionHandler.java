package com.company.timetracking.presentation.handlers;

import com.company.timetracking.domain.exceptions.ActiveWorkSessionExistsException;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.exceptions.DomainException;
import com.company.timetracking.domain.exceptions.EmailAlreadyExistsException;
import com.company.timetracking.domain.exceptions.WorkSessionAlreadyFinishedException;
import com.company.timetracking.domain.exceptions.WorkSessionNotFoundException;
import com.company.timetracking.presentation.responses.ApiError;
import com.company.timetracking.presentation.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Centralized translation of exceptions into the standardized
 * {@link ApiResponse} envelope, with a stable HTTP status per error class.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Domain/business-rule violations — status derived from the exception type. */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomain(DomainException ex) {
        HttpStatus status = statusFor(ex);
        return ResponseEntity.status(status)
                .body(ApiResponse.failure(ApiError.of(ex.code(), ex.getMessage())));
    }

    /** Bean Validation failures on request bodies. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toApiError)
                .toList();
        return ResponseEntity.badRequest().body(ApiResponse.failure(errors));
    }

    /** Malformed/unreadable request body. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.failure(
                ApiError.of("MALFORMED_REQUEST", "Corpo da requisição inválido.")));
    }

    /** Bad path/query argument, e.g. an invalid UUID. */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadArgument(Exception ex) {
        return ResponseEntity.badRequest().body(ApiResponse.failure(
                ApiError.of("INVALID_ARGUMENT", "Parâmetro inválido.")));
    }

    /** Failed authentication. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(
                ApiError.of("INVALID_CREDENTIALS", "Credenciais inválidas.")));
    }

    /** Anything unmapped — never leak internals. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure(
                ApiError.of("INTERNAL_ERROR", "Ocorreu um erro inesperado.")));
    }

    private ApiError toApiError(FieldError fieldError) {
        return ApiError.ofField("VALIDATION_ERROR",
                fieldError.getDefaultMessage(), fieldError.getField());
    }

    private HttpStatus statusFor(DomainException ex) {
        return switch (ex) {
            case CollaboratorNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case WorkSessionNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case EmailAlreadyExistsException ignored -> HttpStatus.CONFLICT;
            case ActiveWorkSessionExistsException ignored -> HttpStatus.CONFLICT;
            case WorkSessionAlreadyFinishedException ignored -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}
