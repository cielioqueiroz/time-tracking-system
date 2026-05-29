package com.company.timetracking.presentation.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCollaboratorRequest(

        @Schema(example = "José Silva")
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String name,

        @Schema(example = "jose.silva@empresa.com")
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @Schema(example = "Desenvolvedor")
        @NotBlank(message = "O cargo é obrigatório.")
        @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
        String cargo
) {}
