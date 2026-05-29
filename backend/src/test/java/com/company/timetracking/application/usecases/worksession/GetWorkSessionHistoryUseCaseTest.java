package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.application.queries.WorkSessionHistoryQuery;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.Page;
import com.company.timetracking.domain.repositories.PageQuery;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWorkSessionHistoryUseCaseTest {

    @Mock
    CollaboratorRepository collaboratorRepository;
    @Mock
    WorkSessionRepository workSessionRepository;

    final WorkSessionDtoMapper mapper = new WorkSessionDtoMapper();

    GetWorkSessionHistoryUseCase useCase() {
        return new GetWorkSessionHistoryUseCase(collaboratorRepository, workSessionRepository, mapper);
    }

    @Test
    void returnsMappedHistoryPage() {
        CollaboratorId cid = CollaboratorId.generate();
        String id = cid.toString();
        WorkSession session = WorkSession.start(cid, Instant.parse("2026-05-29T08:00:00Z"));
        var domainPage = new Page<>(List.of(session), 0, 20, 1L);
        when(collaboratorRepository.existsById(CollaboratorId.of(id))).thenReturn(true);
        when(workSessionRepository.findByCollaborator(any(CollaboratorId.class), any(PageQuery.class)))
                .thenReturn(domainPage);

        var result = useCase().execute(new WorkSessionHistoryQuery(id, 0, 20));

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).collaboratorId()).isEqualTo(id);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(collaboratorRepository.existsById(any(CollaboratorId.class))).thenReturn(false);

        assertThatThrownBy(() -> useCase().execute(
                new WorkSessionHistoryQuery(CollaboratorId.generate().toString(), 0, 20)))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(workSessionRepository, never()).findByCollaborator(any(), any());
    }
}
