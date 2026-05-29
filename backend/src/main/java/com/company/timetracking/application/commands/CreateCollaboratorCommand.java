package com.company.timetracking.application.commands;

/** Write-intent input to create a collaborator. */
public record CreateCollaboratorCommand(String name, String email, String cargo) {}
