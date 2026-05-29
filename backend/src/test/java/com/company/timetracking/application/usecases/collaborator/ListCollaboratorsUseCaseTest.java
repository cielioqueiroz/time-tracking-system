package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.application.queries.ListCollaboratorsQuery;
import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.Page;
import com.company.timetracking.domain.repositories.PageQuery;
import com.company.timetracking.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCollaboratorsUseCaseTest {

    @Mock
    CollaboratorRepository repository;

    final CollaboratorDtoMapper mapper = new CollaboratorDtoMapper();

    ListCollaboratorsUseCase useCase() {
        return new ListCollaboratorsUseCase(repository, mapper);
    }

    @Test
    void returnsMappedPageWithMetadata() {
        var alice = Collaborator.create("Alice", Email.of("alice@empresa.com"));
        var bob = Collaborator.create("Bob", Email.of("bob@empresa.com"));
        var domainPage = new Page<>(List.of(alice, bob), 0, 20, 2L);
        when(repository.findAll(any(PageQuery.class))).thenReturn(domainPage);

        var result = useCase().execute(new ListCollaboratorsQuery(0, 20));

        assertThat(result.content()).hasSize(2);
        assertThat(result.content().get(0).name()).isEqualTo("Alice");
        assertThat(result.content().get(1).name()).isEqualTo("Bob");
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(20);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void returnsEmptyPageWhenNoCollaborators() {
        var domainPage = new Page<Collaborator>(List.of(), 0, 20, 0L);
        when(repository.findAll(any(PageQuery.class))).thenReturn(domainPage);

        var result = useCase().execute(new ListCollaboratorsQuery(0, 20));

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
    }
}
