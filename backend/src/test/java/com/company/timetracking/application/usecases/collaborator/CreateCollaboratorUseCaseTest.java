package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.commands.CreateCollaboratorCommand;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.exceptions.EmailAlreadyExistsException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCollaboratorUseCaseTest {

    @Mock
    CollaboratorRepository repository;

    final CollaboratorDtoMapper mapper = new CollaboratorDtoMapper();

    CreateCollaboratorUseCase useCase() {
        return new CreateCollaboratorUseCase(repository, mapper);
    }

    @Test
    void createsCollaboratorWhenEmailIsFree() {
        when(repository.existsByEmail(any(Email.class))).thenReturn(false);
        when(repository.save(any(Collaborator.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = useCase().execute(
                new CreateCollaboratorCommand("José Silva", "jose@empresa.com", "Desenvolvedor"));

        assertThat(dto.name()).isEqualTo("José Silva");
        assertThat(dto.email()).isEqualTo("jose@empresa.com");
        assertThat(dto.cargo()).isEqualTo("Desenvolvedor");
        verify(repository).save(any(Collaborator.class));
    }

    @Test
    void rejectsDuplicateEmail() {
        when(repository.existsByEmail(any(Email.class))).thenReturn(true);

        assertThatThrownBy(() ->
                useCase().execute(new CreateCollaboratorCommand("José", "jose@empresa.com", "Dev")))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }
}
