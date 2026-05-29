/**
 * Application layer — orchestrates the domain to fulfill use cases.
 *
 * <p>Depends on {@code domain} only. Defines inbound use-case contracts and
 * the DTOs that cross the boundary, but contains no transport or persistence
 * details.
 *
 * <ul>
 *   <li>{@code usecases} — one class per business operation (single responsibility).</li>
 *   <li>{@code commands} — write-intent inputs (CQRS-leaning).</li>
 *   <li>{@code queries}  — read-intent inputs.</li>
 *   <li>{@code dto}      — data carried in/out of use cases.</li>
 *   <li>{@code mappers}  — domain &lt;-&gt; DTO translation.</li>
 *   <li>{@code services} — cross-use-case application services.</li>
 * </ul>
 */
package com.company.timetracking.application;
