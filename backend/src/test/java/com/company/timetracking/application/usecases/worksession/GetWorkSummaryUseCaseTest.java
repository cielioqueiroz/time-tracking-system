package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.queries.WorkSummaryQuery;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.repositories.WorkSessionSummary;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetWorkSummaryUseCaseTest {

    @Mock
    CollaboratorRepository collaboratorRepository;
    @Mock
    WorkSessionRepository workSessionRepository;

    GetWorkSummaryUseCase useCase() {
        return new GetWorkSummaryUseCase(collaboratorRepository, workSessionRepository);
    }

    @Test
    void returnsAggregatedSummaryWhenCollaboratorExists() {
        CollaboratorId cid = CollaboratorId.generate();
        String id = cid.toString();
        when(collaboratorRepository.existsById(CollaboratorId.of(id))).thenReturn(true);
        when(workSessionRepository.summaryByCollaborator(any(CollaboratorId.class), isNull(), isNull()))
                .thenReturn(new WorkSessionSummary(5, 4, 480));

        var dto = useCase().execute(new WorkSummaryQuery(id, null, null));

        assertThat(dto.collaboratorId()).isEqualTo(id);
        assertThat(dto.totalSessions()).isEqualTo(5);
        assertThat(dto.finishedSessions()).isEqualTo(4);
        assertThat(dto.totalMinutes()).isEqualTo(480);
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(collaboratorRepository.existsById(any(CollaboratorId.class))).thenReturn(false);

        assertThatThrownBy(() -> useCase().execute(
                new WorkSummaryQuery(CollaboratorId.generate().toString(), null, null)))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(workSessionRepository, never()).summaryByCollaborator(any(), any(), any());
    }
}
