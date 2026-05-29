package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.commands.UpdateCollaboratorCommand;
import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.exceptions.EmailAlreadyExistsException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Updates a collaborator's name/email, keeping email unique across others. */
@Service
public class UpdateCollaboratorUseCase {

    private final CollaboratorRepository repository;
    private final CollaboratorDtoMapper mapper;

    public UpdateCollaboratorUseCase(CollaboratorRepository repository,
                                     CollaboratorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CollaboratorDto execute(UpdateCollaboratorCommand command) {
        CollaboratorId id = CollaboratorId.of(command.id());
        Collaborator collaborator = repository.findById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException(command.id()));

        Email newEmail = Email.of(command.email());
        boolean emailTakenByAnother = repository.findByEmail(newEmail)
                .filter(other -> !other.id().equals(id))
                .isPresent();
        if (emailTakenByAnother) {
            throw new EmailAlreadyExistsException(newEmail.value());
        }

        collaborator.rename(command.name());
        collaborator.changeEmail(newEmail);
        collaborator.changeCargo(command.cargo());
        return mapper.toDto(repository.save(collaborator));
    }
}
