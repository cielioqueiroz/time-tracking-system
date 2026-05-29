package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.commands.CreateCollaboratorCommand;
import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.exceptions.EmailAlreadyExistsException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCollaboratorUseCase {

    private final CollaboratorRepository repository;
    private final CollaboratorDtoMapper mapper;

    public CreateCollaboratorUseCase(CollaboratorRepository repository,
                                     CollaboratorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CollaboratorDto execute(CreateCollaboratorCommand command) {
        Email email = Email.of(command.email());
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.value());
        }
        Collaborator collaborator = Collaborator.create(command.name(), email, command.cargo());
        return mapper.toDto(repository.save(collaborator));
    }
}
