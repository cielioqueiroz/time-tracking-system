package com.company.timetracking.application.queries;

import java.time.Instant;

public record WorkSummaryQuery(String collaboratorId, Instant from, Instant to) {}
