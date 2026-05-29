package com.company.timetracking.domain.repositories;

import java.util.List;
import java.util.function.Function;

/**
 * Framework-agnostic page of results returned by repository ports.
 *
 * @param <T> element type
 */
public record Page<T>(List<T> content, int page, int size, long totalElements) {

    public int totalPages() {
        return size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
    }

    public boolean hasNext() {
        return page + 1 < totalPages();
    }

    /** Maps each element while preserving pagination metadata. */
    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mapped = content.stream().<R>map(mapper).toList();
        return new Page<>(mapped, page, size, totalElements);
    }
}
