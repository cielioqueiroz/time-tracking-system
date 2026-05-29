package com.company.timetracking.presentation.presenters;

import com.company.timetracking.application.dto.PageDto;
import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.presentation.responses.PageResponse;
import com.company.timetracking.presentation.responses.WorkSessionResponse;
import org.springframework.stereotype.Component;

/** Maps work-session application DTOs to outbound view models. */
@Component
public class WorkSessionPresenter {

    public WorkSessionResponse toResponse(WorkSessionDto dto) {
        return new WorkSessionResponse(
                dto.id(), dto.collaboratorId(), dto.status(),
                dto.startedAt(), dto.endedAt(), dto.totalMinutes());
    }

    public PageResponse<WorkSessionResponse> toPageResponse(PageDto<WorkSessionDto> page) {
        return PageResponse.from(page, this::toResponse);
    }
}
