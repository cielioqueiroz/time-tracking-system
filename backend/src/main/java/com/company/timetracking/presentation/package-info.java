/**
 * Presentation layer — inbound REST adapters.
 *
 * <p>Translates HTTP into use-case invocations and back into the standardized
 * {@link com.company.timetracking.presentation.responses.ApiResponse} envelope.
 * Contains no business logic.
 *
 * <ul>
 *   <li>{@code controllers} — thin REST endpoints delegating to use cases.</li>
 *   <li>{@code requests}    — inbound request bodies + Bean Validation.</li>
 *   <li>{@code responses}   — outbound view models &amp; the response envelope.</li>
 *   <li>{@code presenters}  — map application DTOs to response view models.</li>
 *   <li>{@code handlers}    — global exception handling.</li>
 * </ul>
 */
package com.company.timetracking.presentation;
