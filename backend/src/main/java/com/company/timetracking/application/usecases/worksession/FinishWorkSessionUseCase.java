package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.commands.FinishWorkSessionCommand;
import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.exceptions.WorkSessionNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class FinishWorkSessionUseCase {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkSessionRepository workSessionRepository;
    private final WorkSessionDtoMapper mapper;
    private final Clock clock;

    public FinishWorkSessionUseCase(CollaboratorRepository collaboratorRepository,
                                    WorkSessionRepository workSessionRepository,
                                    WorkSessionDtoMapper mapper,
                                    Clock clock) {
        this.collaboratorRepository = collaboratorRepository;
        this.workSessionRepository = workSessionRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional
    public WorkSessionDto execute(FinishWorkSessionCommand command) {
        CollaboratorId collaboratorId = CollaboratorId.of(command.collaboratorId());
        Collaborator collaborator = collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException(command.collaboratorId()));

        WorkSession session = workSessionRepository.findActiveByCollaborator(collaboratorId)
                .orElseThrow(() -> new WorkSessionNotFoundException(
                        "nenhuma jornada em andamento para o colaborador " + command.collaboratorId()));

        session.finish(clock.instant());
        WorkSession saved = workSessionRepository.save(session);

        collaborator.markOffDuty();
        collaboratorRepository.save(collaborator);

        return mapper.toDto(saved);
    }
}
