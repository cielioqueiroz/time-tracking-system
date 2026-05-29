package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.commands.FinishWorkSessionCommand;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.enums.CollaboratorStatus;
import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.exceptions.WorkSessionNotFoundException;
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
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinishWorkSessionUseCaseTest {

    @Mock
    CollaboratorRepository collaboratorRepository;
    @Mock
    WorkSessionRepository workSessionRepository;

    final WorkSessionDtoMapper mapper = new WorkSessionDtoMapper();
    final Instant start = Instant.parse("2026-05-29T08:00:00Z");
    final Clock clock = Clock.fixed(start.plus(2, ChronoUnit.HOURS), ZoneOffset.UTC);

    FinishWorkSessionUseCase useCase() {
        return new FinishWorkSessionUseCase(collaboratorRepository, workSessionRepository, mapper, clock);
    }

    @Test
    void finishesSessionComputingMinutesAndMarksOffDuty() {
        Collaborator collaborator = Collaborator.create("Ana", Email.of("ana@empresa.com"), "Dev");
        collaborator.markWorking();
        CollaboratorId cid = collaborator.id();
        String id = cid.toString();
        WorkSession open = WorkSession.start(cid, start);
        when(collaboratorRepository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(collaborator));
        when(workSessionRepository.findActiveByCollaborator(CollaboratorId.of(id))).thenReturn(Optional.of(open));
        when(workSessionRepository.save(any(WorkSession.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = useCase().execute(new FinishWorkSessionCommand(id));

        assertThat(dto.status()).isEqualTo(WorkSessionStatus.FINALIZADA);
        assertThat(dto.totalMinutes()).isEqualTo(120L);
        assertThat(dto.endedAt()).isEqualTo(start.plus(2, ChronoUnit.HOURS));
        assertThat(collaborator.status()).isEqualTo(CollaboratorStatus.FORA_DA_JORNADA);
        verify(collaboratorRepository).save(collaborator);
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(collaboratorRepository.findById(any(CollaboratorId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase().execute(
                new FinishWorkSessionCommand(CollaboratorId.generate().toString())))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(workSessionRepository, never()).save(any());
    }

    @Test
    void rejectsWhenNoActiveSession() {
        Collaborator collaborator = Collaborator.create("Bob", Email.of("bob@empresa.com"), "QA");
        String id = collaborator.id().toString();
        when(collaboratorRepository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(collaborator));
        when(workSessionRepository.findActiveByCollaborator(CollaboratorId.of(id))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase().execute(new FinishWorkSessionCommand(id)))
                .isInstanceOf(WorkSessionNotFoundException.class);

        verify(workSessionRepository, never()).save(any());
    }
}
