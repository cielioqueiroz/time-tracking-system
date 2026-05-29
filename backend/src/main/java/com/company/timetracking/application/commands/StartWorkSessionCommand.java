package com.company.timetracking.application.commands;

/** Write-intent input to start a journey for a collaborator. */
public record StartWorkSessionCommand(String collaboratorId) {}
