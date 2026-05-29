package com.company.timetracking.application.commands;

/** Write-intent input to update an existing collaborator. */
public record UpdateCollaboratorCommand(String id, String name, String email, String cargo) {}
