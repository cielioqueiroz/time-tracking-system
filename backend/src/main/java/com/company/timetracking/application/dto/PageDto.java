package com.company.timetracking.application.dto;

import com.company.timetracking.domain.repositories.Page;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageDto<T> from(Page<T> page) {
        return new PageDto<>(page.content(), page.page(), page.size(),
                page.totalElements(), page.totalPages());
    }
}
