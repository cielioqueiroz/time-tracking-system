package com.company.timetracking.domain.valueobjects;

import java.util.UUID;

public record WorkSessionId(UUID value) {

    public WorkSessionId {
        if (value == null) {
            throw new IllegalArgumentException("WorkSessionId não pode ser nulo");
        }
    }

    public static WorkSessionId generate() {
        return new WorkSessionId(UUID.randomUUID());
    }

    public static WorkSessionId of(UUID value) {
        return new WorkSessionId(value);
    }

    public static WorkSessionId of(String value) {
        return new WorkSessionId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
