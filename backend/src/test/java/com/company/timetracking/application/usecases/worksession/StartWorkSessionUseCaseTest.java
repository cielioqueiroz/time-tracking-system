package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.commands.StartWorkSessionCommand;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.enums.CollaboratorStatus;
import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.domain.exceptions.ActiveWorkSessionExistsException;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartWorkSessionUseCaseTest {

    @Mock
    CollaboratorRepository collaboratorRepository;
    @Mock
    WorkSessionRepository workSessionRepository;

    final WorkSessionDtoMapper mapper = new WorkSessionDtoMapper();
    final Clock clock = Clock.fixed(Instant.parse("2026-05-29T08:00:00Z"), ZoneOffset.UTC);

    StartWorkSessionUseCase useCase() {
        return new StartWorkSessionUseCase(collaboratorRepository, workSessionRepository, mapper, clock);
    }

    @Test
    void startsSessionAndMarksCollaboratorWorking() {
        Collaborator collaborator = Collaborator.create("Ana", Email.of("ana@empresa.com"), "Dev");
        String id = collaborator.id().toString();
        when(collaboratorRepository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(collaborator));
        when(workSessionRepository.existsActiveByCollaborator(CollaboratorId.of(id))).thenReturn(false);
        when(workSessionRepository.save(any(WorkSession.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = useCase().execute(new StartWorkSessionCommand(id));

        assertThat(dto.status()).isEqualTo(WorkSessionStatus.EM_ANDAMENTO);
        assertThat(dto.collaboratorId()).isEqualTo(id);
        assertThat(dto.startedAt()).isEqualTo(Instant.parse("2026-05-29T08:00:00Z"));
        assertThat(collaborator.status()).isEqualTo(CollaboratorStatus.TRABALHANDO);
        verify(workSessionRepository).save(any(WorkSession.class));
        verify(collaboratorRepository).save(collaborator);
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(collaboratorRepository.findById(any(CollaboratorId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase().execute(
                new StartWorkSessionCommand(CollaboratorId.generate().toString())))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(workSessionRepository, never()).save(any());
    }

    @Test
    void rejectsWhenActiveSessionAlreadyExists() {
        Collaborator collaborator = Collaborator.create("Bob", Email.of("bob@empresa.com"), "QA");
        String id = collaborator.id().toString();
        when(collaboratorRepository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(collaborator));
        when(workSessionRepository.existsActiveByCollaborator(CollaboratorId.of(id))).thenReturn(true);

        assertThatThrownBy(() -> useCase().execute(new StartWorkSessionCommand(id)))
                .isInstanceOf(ActiveWorkSessionExistsException.class);

        verify(workSessionRepository, never()).save(any());
    }
}
