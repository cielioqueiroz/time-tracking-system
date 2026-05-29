package com.company.timetracking.presentation.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(example = "admin")
        @NotBlank(message = "O usuário é obrigatório.")
        String username,

        @Schema(example = "admin")
        @NotBlank(message = "A senha é obrigatória.")
        String password
) {}
