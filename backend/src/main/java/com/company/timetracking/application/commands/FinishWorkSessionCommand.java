package com.company.timetracking.application.commands;

/** Write-intent input to finish the open journey of a collaborator. */
public record FinishWorkSessionCommand(String collaboratorId) {}
