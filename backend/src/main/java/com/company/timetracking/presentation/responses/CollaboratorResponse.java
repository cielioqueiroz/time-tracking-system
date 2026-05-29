package com.company.timetracking.presentation.responses;

import com.company.timetracking.domain.enums.CollaboratorStatus;

public record CollaboratorResponse(
        String id,
        String name,
        String email,
        String cargo,
        CollaboratorStatus status
) {}
