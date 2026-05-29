/**
 * Infrastructure layer — outbound adapters and framework wiring.
 *
 * <p>Implements the ports declared in {@code domain.repositories} and provides
 * cross-cutting technical concerns. Depends inward on {@code domain} (and
 * {@code application}); the inner layers never depend on this package.
 *
 * <ul>
 *   <li>{@code persistence.jpa} — JPA entities, Spring Data repositories,
 *       and mappers between JPA entities and domain models.</li>
 *   <li>{@code config}        — Spring beans &amp; technical configuration.</li>
 *   <li>{@code security}      — authentication/authorization adapters (JWT).</li>
 *   <li>{@code exceptions}    — infrastructure-level error translation.</li>
 *   <li>{@code external}      — clients for third-party systems.</li>
 *   <li>{@code documentation} — OpenAPI / Swagger configuration.</li>
 * </ul>
 */
package com.company.timetracking.infrastructure;
