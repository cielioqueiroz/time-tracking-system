package com.company.timetracking.presentation.presenters;

import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.application.dto.PageDto;
import com.company.timetracking.presentation.responses.CollaboratorResponse;
import com.company.timetracking.presentation.responses.PageResponse;
import org.springframework.stereotype.Component;

/** Maps collaborator application DTOs to outbound view models. */
@Component
public class CollaboratorPresenter {

    public CollaboratorResponse toResponse(CollaboratorDto dto) {
        return new CollaboratorResponse(dto.id(), dto.name(), dto.email(), dto.cargo(), dto.status());
    }

    public PageResponse<CollaboratorResponse> toPageResponse(PageDto<CollaboratorDto> page) {
        return PageResponse.from(page, this::toResponse);
    }
}
