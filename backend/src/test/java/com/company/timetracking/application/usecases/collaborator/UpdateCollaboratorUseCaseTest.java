package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.commands.UpdateCollaboratorCommand;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.exceptions.EmailAlreadyExistsException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCollaboratorUseCaseTest {

    @Mock
    CollaboratorRepository repository;

    final CollaboratorDtoMapper mapper = new CollaboratorDtoMapper();

    UpdateCollaboratorUseCase useCase() {
        return new UpdateCollaboratorUseCase(repository, mapper);
    }

    @Test
    void updatesNameAndEmailWhenEmailIsFree() {
        Collaborator existing = Collaborator.create("Nome Antigo", Email.of("antigo@empresa.com"), "Dev");
        String id = existing.id().toString();
        when(repository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(existing));
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
        when(repository.save(any(Collaborator.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = useCase().execute(
                new UpdateCollaboratorCommand(id, "Nome Novo", "novo@empresa.com", "Tech Lead"));

        assertThat(dto.name()).isEqualTo("Nome Novo");
        assertThat(dto.email()).isEqualTo("novo@empresa.com");
        assertThat(dto.cargo()).isEqualTo("Tech Lead");
        verify(repository).save(any(Collaborator.class));
    }

    @Test
    void allowsKeepingOwnEmail() {
        Collaborator existing = Collaborator.create("João", Email.of("joao@empresa.com"), "Dev");
        String id = existing.id().toString();
        when(repository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(existing));
        // findByEmail returns the same collaborator → not taken by another
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(existing));
        when(repository.save(any(Collaborator.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = useCase().execute(
                new UpdateCollaboratorCommand(id, "João Atualizado", "joao@empresa.com", "Dev"));

        assertThat(dto.name()).isEqualTo("João Atualizado");
        assertThat(dto.email()).isEqualTo("joao@empresa.com");
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(repository.findById(any(CollaboratorId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase().execute(
                new UpdateCollaboratorCommand(CollaboratorId.generate().toString(), "X", "x@empresa.com", "Dev")))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void rejectsWhenEmailBelongsToAnotherCollaborator() {
        Collaborator existing = Collaborator.create("Maria", Email.of("maria@empresa.com"), "Dev");
        String id = existing.id().toString();
        Collaborator another = Collaborator.create("Outra", Email.of("ocupado@empresa.com"), "Dev");
        when(repository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(existing));
        when(repository.findByEmail(any(Email.class))).thenReturn(Optional.of(another));

        assertThatThrownBy(() -> useCase().execute(
                new UpdateCollaboratorCommand(id, "Maria", "ocupado@empresa.com", "Dev")))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }
}
