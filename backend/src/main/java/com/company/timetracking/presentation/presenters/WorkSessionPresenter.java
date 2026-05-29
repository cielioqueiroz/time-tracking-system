package com.company.timetracking.presentation.presenters;

import com.company.timetracking.application.dto.PageDto;
import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.application.dto.WorkSummaryDto;
import com.company.timetracking.presentation.responses.PageResponse;
import com.company.timetracking.presentation.responses.WorkSessionResponse;
import com.company.timetracking.presentation.responses.WorkSummaryResponse;
import org.springframework.stereotype.Component;

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

    public WorkSummaryResponse toSummaryResponse(WorkSummaryDto dto) {
        return new WorkSummaryResponse(
                dto.collaboratorId(), dto.totalSessions(),
                dto.finishedSessions(), dto.totalMinutes());
    }
}
