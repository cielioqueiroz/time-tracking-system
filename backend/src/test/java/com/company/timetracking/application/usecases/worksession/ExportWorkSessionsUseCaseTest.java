package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
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
class ExportWorkSessionsUseCaseTest {

    @Mock
    CollaboratorRepository collaboratorRepository;
    @Mock
    WorkSessionRepository workSessionRepository;

    final WorkSessionDtoMapper mapper = new WorkSessionDtoMapper();

    ExportWorkSessionsUseCase useCase() {
        return new ExportWorkSessionsUseCase(collaboratorRepository, workSessionRepository, mapper);
    }

    @Test
    void returnsMappedSessionsWhenCollaboratorExists() {
        CollaboratorId cid = CollaboratorId.generate();
        String id = cid.toString();
        WorkSession session = WorkSession.start(cid, Instant.parse("2026-05-29T08:00:00Z"));
        when(collaboratorRepository.existsById(CollaboratorId.of(id))).thenReturn(true);
        when(workSessionRepository.findAllByCollaborator(any(CollaboratorId.class)))
                .thenReturn(List.of(session));

        var result = useCase().execute(id);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).collaboratorId()).isEqualTo(id);
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(collaboratorRepository.existsById(any(CollaboratorId.class))).thenReturn(false);

        assertThatThrownBy(() -> useCase().execute(CollaboratorId.generate().toString()))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(workSessionRepository, never()).findAllByCollaborator(any());
    }
}
