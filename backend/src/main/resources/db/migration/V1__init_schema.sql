CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE collaborators (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(150) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    status      VARCHAR(32)  NOT NULL DEFAULT 'FORA_DA_JORNADA',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT uq_collaborators_email UNIQUE (email),
    CONSTRAINT ck_collaborators_status
        CHECK (status IN ('TRABALHANDO', 'FORA_DA_JORNADA'))
);

CREATE UNIQUE INDEX uq_collaborators_email_lower ON collaborators (lower(email));

CREATE TABLE work_sessions (
    id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    collaborator_id  UUID         NOT NULL,
    status           VARCHAR(32)  NOT NULL DEFAULT 'EM_ANDAMENTO',
    started_at       TIMESTAMPTZ  NOT NULL,
    ended_at         TIMESTAMPTZ,
    total_minutes    BIGINT,
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT fk_work_sessions_collaborator
        FOREIGN KEY (collaborator_id) REFERENCES collaborators (id) ON DELETE CASCADE,
    CONSTRAINT ck_work_sessions_status
        CHECK (status IN ('EM_ANDAMENTO', 'FINALIZADA')),

    CONSTRAINT ck_work_sessions_consistency CHECK (
        (status = 'EM_ANDAMENTO' AND ended_at IS NULL  AND total_minutes IS NULL) OR
        (status = 'FINALIZADA'   AND ended_at IS NOT NULL AND total_minutes IS NOT NULL)
    ),
    CONSTRAINT ck_work_sessions_period CHECK (ended_at IS NULL OR ended_at >= started_at)
);

CREATE UNIQUE INDEX uq_work_sessions_one_open
    ON work_sessions (collaborator_id)
    WHERE status = 'EM_ANDAMENTO';

CREATE INDEX ix_work_sessions_collaborator_started
    ON work_sessions (collaborator_id, started_at DESC);
