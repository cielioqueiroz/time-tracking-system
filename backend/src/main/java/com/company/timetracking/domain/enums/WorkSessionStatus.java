package com.company.timetracking.domain.enums;

/**
 * Lifecycle state of a {@code WorkSession}.
 */
public enum WorkSessionStatus {
    /** Started and not yet finished. */
    EM_ANDAMENTO,
    /** Finished; has an end time and a computed duration. */
    FINALIZADA
}
