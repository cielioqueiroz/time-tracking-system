package com.company.timetracking.application.commands;

public record UpdateCollaboratorCommand(String id, String name, String email, String cargo) {}
