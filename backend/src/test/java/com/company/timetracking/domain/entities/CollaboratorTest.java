package com.company.timetracking.domain.entities;

import com.company.timetracking.domain.enums.CollaboratorStatus;
import com.company.timetracking.domain.exceptions.InvalidCollaboratorDataException;
import com.company.timetracking.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CollaboratorTest {

    @Test
    void newCollaboratorStartsOffDuty() {
        var c = Collaborator.create("José Silva", Email.of("jose@empresa.com"));
        assertThat(c.status()).isEqualTo(CollaboratorStatus.FORA_DA_JORNADA);
        assertThat(c.name()).isEqualTo("José Silva");
    }

    @Test
    void rejectsBlankName() {
        assertThatThrownBy(() -> Collaborator.create("  ", Email.of("jose@empresa.com")))
                .isInstanceOf(InvalidCollaboratorDataException.class);
    }

    @Test
    void statusTransitionsAreExplicit() {
        var c = Collaborator.create("Maria Oliveira", Email.of("maria@empresa.com"));
        c.markWorking();
        assertThat(c.status()).isEqualTo(CollaboratorStatus.TRABALHANDO);
        c.markOffDuty();
        assertThat(c.status()).isEqualTo(CollaboratorStatus.FORA_DA_JORNADA);
    }

    @Test
    void trimsNameOnRename() {
        var c = Collaborator.create("José", Email.of("jose@empresa.com"));
        c.rename("  Carlos Souza  ");
        assertThat(c.name()).isEqualTo("Carlos Souza");
    }
}
