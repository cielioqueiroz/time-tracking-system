/**
 * Domain layer — the framework-agnostic core of the system.
 *
 * <p>Holds entities, value objects, enums, repository <em>ports</em>
 * (interfaces), domain services and domain exceptions. It depends on
 * <strong>nothing</strong> outside itself (no Spring, no JPA, no web).
 *
 * <ul>
 *   <li>{@code entities}     — aggregates &amp; entities with their invariants.</li>
 *   <li>{@code valueobjects} — immutable, self-validating values (e.g. Email).</li>
 *   <li>{@code enums}        — domain enumerations (statuses).</li>
 *   <li>{@code repositories} — outbound ports implemented by infrastructure.</li>
 *   <li>{@code services}     — domain logic spanning multiple entities.</li>
 *   <li>{@code exceptions}   — domain-specific error types.</li>
 * </ul>
 */
package com.company.timetracking.domain;
