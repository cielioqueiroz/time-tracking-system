package com.company.timetracking.domain.entities;

import com.company.timetracking.domain.enums.CollaboratorStatus;
import com.company.timetracking.domain.exceptions.InvalidCollaboratorDataException;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;

import java.util.Objects;

/**
 * Collaborator aggregate root.
 *
 * <p>Encapsulates its own invariants (name and cargo required, valid email) and
 * the transitions of its working status. State is never mutated from the outside
 * except through these intention-revealing methods.
 */
public class Collaborator {

    private static final int NAME_MAX = 150;
    private static final int CARGO_MAX = 100;

    private final CollaboratorId id;
    private String name;
    private Email email;
    private String cargo;
    private CollaboratorStatus status;

    private Collaborator(CollaboratorId id, String name, Email email, String cargo,
                         CollaboratorStatus status) {
        this.id = Objects.requireNonNull(id, "id");
        this.email = Objects.requireNonNull(email, "email");
        this.status = Objects.requireNonNull(status, "status");
        rename(name);
        changeCargo(cargo);
    }

    /** Creates a brand-new collaborator, off-duty by default. */
    public static Collaborator create(String name, Email email, String cargo) {
        return new Collaborator(CollaboratorId.generate(), name, email, cargo,
                CollaboratorStatus.FORA_DA_JORNADA);
    }

    /** Reconstructs an existing collaborator (e.g. from persistence). */
    public static Collaborator restore(CollaboratorId id, String name, Email email, String cargo,
                                       CollaboratorStatus status) {
        return new Collaborator(id, name, email, cargo, status);
    }

    public void rename(String newName) {
        String normalized = newName == null ? "" : newName.trim();
        if (normalized.isBlank()) {
            throw new InvalidCollaboratorDataException("O nome é obrigatório.");
        }
        if (normalized.length() > NAME_MAX) {
            throw new InvalidCollaboratorDataException(
                    "O nome deve ter no máximo " + NAME_MAX + " caracteres.");
        }
        this.name = normalized;
    }

    public void changeCargo(String newCargo) {
        String normalized = newCargo == null ? "" : newCargo.trim();
        if (normalized.isBlank()) {
            throw new InvalidCollaboratorDataException("O cargo é obrigatório.");
        }
        if (normalized.length() > CARGO_MAX) {
            throw new InvalidCollaboratorDataException(
                    "O cargo deve ter no máximo " + CARGO_MAX + " caracteres.");
        }
        this.cargo = normalized;
    }

    public void changeEmail(Email newEmail) {
        this.email = Objects.requireNonNull(newEmail, "email");
    }

    /** Marks the collaborator as working (called when a session starts). */
    public void markWorking() {
        this.status = CollaboratorStatus.TRABALHANDO;
    }

    /** Marks the collaborator as off-duty (called when a session ends). */
    public void markOffDuty() {
        this.status = CollaboratorStatus.FORA_DA_JORNADA;
    }

    public CollaboratorId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Email email() {
        return email;
    }

    /** Job title of the collaborator (e.g. "Desenvolvedor"). */
    public String cargo() {
        return cargo;
    }

    public CollaboratorStatus status() {
        return status;
    }
}
