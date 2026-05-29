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
        var c = Collaborator.create("José Silva", Email.of("jose@empresa.com"), "Desenvolvedor");
        assertThat(c.status()).isEqualTo(CollaboratorStatus.FORA_DA_JORNADA);
        assertThat(c.name()).isEqualTo("José Silva");
        assertThat(c.cargo()).isEqualTo("Desenvolvedor");
    }

    @Test
    void rejectsBlankName() {
        assertThatThrownBy(() -> Collaborator.create("  ", Email.of("jose@empresa.com"), "Dev"))
                .isInstanceOf(InvalidCollaboratorDataException.class);
    }

    @Test
    void rejectsBlankCargo() {
        assertThatThrownBy(() -> Collaborator.create("José", Email.of("jose@empresa.com"), "  "))
                .isInstanceOf(InvalidCollaboratorDataException.class);
    }

    @Test
    void statusTransitionsAreExplicit() {
        var c = Collaborator.create("Maria Oliveira", Email.of("maria@empresa.com"), "Gerente");
        c.markWorking();
        assertThat(c.status()).isEqualTo(CollaboratorStatus.TRABALHANDO);
        c.markOffDuty();
        assertThat(c.status()).isEqualTo(CollaboratorStatus.FORA_DA_JORNADA);
    }

    @Test
    void trimsNameAndCargo() {
        var c = Collaborator.create("José", Email.of("jose@empresa.com"), "Dev");
        c.rename("  Carlos Souza  ");
        c.changeCargo("  Tech Lead  ");
        assertThat(c.name()).isEqualTo("Carlos Souza");
        assertThat(c.cargo()).isEqualTo("Tech Lead");
    }
}
