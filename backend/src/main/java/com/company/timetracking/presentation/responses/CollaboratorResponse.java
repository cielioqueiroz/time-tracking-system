package com.company.timetracking.presentation.responses;

import com.company.timetracking.domain.enums.CollaboratorStatus;

/** Outbound view model for a collaborator. */
public record CollaboratorResponse(
        String id,
        String name,
        String email,
        CollaboratorStatus status
) {}
