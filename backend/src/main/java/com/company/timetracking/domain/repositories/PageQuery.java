package com.company.timetracking.domain.repositories;

/**
 * Framework-agnostic pagination request used by repository ports, so the
 * domain never depends on Spring's {@code Pageable}.
 */
public record PageQuery(int page, int size) {

    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public PageQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = DEFAULT_SIZE;
        if (size > MAX_SIZE) size = MAX_SIZE;
    }

    public static PageQuery of(int page, int size) {
        return new PageQuery(page, size);
    }
}
