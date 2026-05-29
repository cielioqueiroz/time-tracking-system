-- ════════════════════════════════════════════════════════════════════════
--  V2 — Add the mandatory "cargo" (job title) to collaborators.
--  Existing rows are backfilled with a placeholder; the default is then
--  dropped so new inserts must provide a value (the app always does).
-- ════════════════════════════════════════════════════════════════════════

ALTER TABLE collaborators
    ADD COLUMN cargo VARCHAR(100) NOT NULL DEFAULT 'Não informado';

ALTER TABLE collaborators
    ALTER COLUMN cargo DROP DEFAULT;
