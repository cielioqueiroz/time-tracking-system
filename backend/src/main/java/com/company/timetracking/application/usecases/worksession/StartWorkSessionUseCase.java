package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.commands.StartWorkSessionCommand;
import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.exceptions.ActiveWorkSessionExistsException;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

/**
 * Starts a journey for a collaborator.
 *
 * <p>Business rules enforced:
 * <ul>
 *   <li>collaborator must exist;</li>
 *   <li>no two simultaneous journeys — fails if one is already open;</li>
 *   <li>collaborator status flips to {@code TRABALHANDO} automatically.</li>
 * </ul>
 */
@Service
public class StartWorkSessionUseCase {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkSessionRepository workSessionRepository;
    private final WorkSessionDtoMapper mapper;
    private final Clock clock;

    public StartWorkSessionUseCase(CollaboratorRepository collaboratorRepository,
                                   WorkSessionRepository workSessionRepository,
                                   WorkSessionDtoMapper mapper,
                                   Clock clock) {
        this.collaboratorRepository = collaboratorRepository;
        this.workSessionRepository = workSessionRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Transactional
    public WorkSessionDto execute(StartWorkSessionCommand command) {
        CollaboratorId collaboratorId = CollaboratorId.of(command.collaboratorId());
        Collaborator collaborator = collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new CollaboratorNotFoundException(command.collaboratorId()));

        if (workSessionRepository.existsActiveByCollaborator(collaboratorId)) {
            throw new ActiveWorkSessionExistsException(command.collaboratorId());
        }

        WorkSession session = WorkSession.start(collaboratorId, clock.instant());
        WorkSession saved = workSessionRepository.save(session);

        collaborator.markWorking();
        collaboratorRepository.save(collaborator);

        return mapper.toDto(saved);
    }
}
