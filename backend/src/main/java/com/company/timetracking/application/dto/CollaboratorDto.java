package com.company.timetracking.application.dto;

import com.company.timetracking.domain.enums.CollaboratorStatus;

/** Application-level read model for a collaborator. */
public record CollaboratorDto(
        String id,
        String name,
        String email,
        String cargo,
        CollaboratorStatus status
) {}
