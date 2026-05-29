ALTER TABLE collaborators
    ADD COLUMN cargo VARCHAR(100) NOT NULL DEFAULT 'Não informado';

ALTER TABLE collaborators
    ALTER COLUMN cargo DROP DEFAULT;
