package com.company.timetracking.presentation.responses;

import com.company.timetracking.application.dto.PageDto;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <S, T> PageResponse<T> from(PageDto<S> dto, Function<S, T> itemMapper) {
        return new PageResponse<>(
                dto.content().stream().map(itemMapper).toList(),
                dto.page(),
                dto.size(),
                dto.totalElements(),
                dto.totalPages()
        );
    }
}
