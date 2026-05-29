package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCollaboratorUseCaseTest {

    @Mock
    CollaboratorRepository repository;

    DeleteCollaboratorUseCase useCase() {
        return new DeleteCollaboratorUseCase(repository);
    }

    @Test
    void deletesWhenCollaboratorExists() {
        String id = CollaboratorId.generate().toString();
        when(repository.existsById(CollaboratorId.of(id))).thenReturn(true);

        useCase().execute(id);

        verify(repository).deleteById(CollaboratorId.of(id));
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        String id = CollaboratorId.generate().toString();
        when(repository.existsById(any(CollaboratorId.class))).thenReturn(false);

        assertThatThrownBy(() -> useCase().execute(id))
                .isInstanceOf(CollaboratorNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
