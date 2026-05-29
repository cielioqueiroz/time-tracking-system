package com.company.timetracking.domain.valueobjects;

import java.util.UUID;

public record CollaboratorId(UUID value) {

    public CollaboratorId {
        if (value == null) {
            throw new IllegalArgumentException("CollaboratorId não pode ser nulo");
        }
    }

    public static CollaboratorId generate() {
        return new CollaboratorId(UUID.randomUUID());
    }

    public static CollaboratorId of(UUID value) {
        return new CollaboratorId(value);
    }

    public static CollaboratorId of(String value) {
        return new CollaboratorId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
