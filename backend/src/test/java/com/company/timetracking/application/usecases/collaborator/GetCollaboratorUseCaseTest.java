package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCollaboratorUseCaseTest {

    @Mock
    CollaboratorRepository repository;

    final CollaboratorDtoMapper mapper = new CollaboratorDtoMapper();

    GetCollaboratorUseCase useCase() {
        return new GetCollaboratorUseCase(repository, mapper);
    }

    @Test
    void returnsCollaboratorWhenFound() {
        Collaborator existing = Collaborator.create("Ana", Email.of("ana@empresa.com"), "Analista");
        String id = existing.id().toString();
        when(repository.findById(CollaboratorId.of(id))).thenReturn(Optional.of(existing));

        var dto = useCase().execute(id);

        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo("Ana");
        assertThat(dto.email()).isEqualTo("ana@empresa.com");
        assertThat(dto.cargo()).isEqualTo("Analista");
    }

    @Test
    void rejectsWhenCollaboratorNotFound() {
        when(repository.findById(any(CollaboratorId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase().execute(CollaboratorId.generate().toString()))
                .isInstanceOf(CollaboratorNotFoundException.class);
    }
}
