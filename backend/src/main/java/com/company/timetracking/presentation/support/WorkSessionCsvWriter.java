package com.company.timetracking.presentation.support;

import com.company.timetracking.application.dto.WorkSessionDto;

import java.util.List;

/**
 * Renders work sessions as RFC-4180-ish CSV (semicolon-separated, fields quoted
 * and escaped) — semicolon plays nicer with spreadsheets in pt-BR locales.
 */
public final class WorkSessionCsvWriter {

    private static final String SEP = ";";
    private static final String EOL = "\r\n";
    private static final String[] HEADERS = {"id", "status", "inicio", "fim", "minutos"};

    private WorkSessionCsvWriter() {
    }

    public static String toCsv(List<WorkSessionDto> sessions) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(SEP, HEADERS)).append(EOL);
        for (WorkSessionDto s : sessions) {
            sb.append(quote(s.id())).append(SEP)
                    .append(quote(s.status().name())).append(SEP)
                    .append(quote(s.startedAt() == null ? "" : s.startedAt().toString())).append(SEP)
                    .append(quote(s.endedAt() == null ? "" : s.endedAt().toString())).append(SEP)
                    .append(quote(s.totalMinutes() == null ? "" : s.totalMinutes().toString()))
                    .append(EOL);
        }
        return sb.toString();
    }

    private static String quote(String value) {
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
