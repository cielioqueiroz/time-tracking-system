package com.company.timetracking.domain.entities;

import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.domain.exceptions.WorkSessionAlreadyFinishedException;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.WorkPeriod;
import com.company.timetracking.domain.valueobjects.WorkSessionId;

import java.time.Instant;
import java.util.Objects;

public class WorkSession {

    private final WorkSessionId id;
    private final CollaboratorId collaboratorId;
    private WorkSessionStatus status;
    private WorkPeriod period;

    private WorkSession(WorkSessionId id, CollaboratorId collaboratorId,
                        WorkSessionStatus status, WorkPeriod period) {
        this.id = Objects.requireNonNull(id, "id");
        this.collaboratorId = Objects.requireNonNull(collaboratorId, "collaboratorId");
        this.status = Objects.requireNonNull(status, "status");
        this.period = Objects.requireNonNull(period, "period");
    }

    public static WorkSession start(CollaboratorId collaboratorId, Instant now) {
        return new WorkSession(WorkSessionId.generate(), collaboratorId,
                WorkSessionStatus.EM_ANDAMENTO, WorkPeriod.startingAt(now));
    }

    public static WorkSession restore(WorkSessionId id, CollaboratorId collaboratorId,
                                      WorkSessionStatus status, WorkPeriod period) {
        return new WorkSession(id, collaboratorId, status, period);
    }

    public void finish(Instant now) {
        if (status == WorkSessionStatus.FINALIZADA) {
            throw new WorkSessionAlreadyFinishedException(id.toString());
        }
        this.period = period.closeAt(now);
        this.status = WorkSessionStatus.FINALIZADA;
    }

    public boolean isOpen() {
        return status == WorkSessionStatus.EM_ANDAMENTO;
    }

    public WorkSessionId id() {
        return id;
    }

    public CollaboratorId collaboratorId() {
        return collaboratorId;
    }

    public WorkSessionStatus status() {
        return status;
    }

    public WorkPeriod period() {
        return period;
    }

    public Instant startedAt() {
        return period.startedAt();
    }

    public Instant endedAt() {
        return period.endedAt();
    }

    public Long totalMinutes() {
        return period.totalMinutes();
    }
}
