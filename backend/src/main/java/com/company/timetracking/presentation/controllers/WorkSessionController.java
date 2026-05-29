package com.company.timetracking.presentation.controllers;

import com.company.timetracking.application.commands.FinishWorkSessionCommand;
import com.company.timetracking.application.commands.StartWorkSessionCommand;
import com.company.timetracking.application.queries.WorkSessionHistoryQuery;
import com.company.timetracking.application.usecases.worksession.FinishWorkSessionUseCase;
import com.company.timetracking.application.usecases.worksession.GetWorkSessionHistoryUseCase;
import com.company.timetracking.application.usecases.worksession.StartWorkSessionUseCase;
import com.company.timetracking.presentation.presenters.WorkSessionPresenter;
import com.company.timetracking.presentation.responses.ApiResponse;
import com.company.timetracking.presentation.responses.PageResponse;
import com.company.timetracking.presentation.responses.WorkSessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for a collaborator's work sessions (journeys).
 */
@RestController
@RequestMapping("/api/v1/collaborators/{collaboratorId}/work-sessions")
@Tag(name = "Work Sessions", description = "Início, encerramento e histórico de jornadas")
public class WorkSessionController {

    private final StartWorkSessionUseCase startUseCase;
    private final FinishWorkSessionUseCase finishUseCase;
    private final GetWorkSessionHistoryUseCase historyUseCase;
    private final WorkSessionPresenter presenter;

    public WorkSessionController(StartWorkSessionUseCase startUseCase,
                                 FinishWorkSessionUseCase finishUseCase,
                                 GetWorkSessionHistoryUseCase historyUseCase,
                                 WorkSessionPresenter presenter) {
        this.startUseCase = startUseCase;
        this.finishUseCase = finishUseCase;
        this.historyUseCase = historyUseCase;
        this.presenter = presenter;
    }

    @Operation(summary = "Inicia a jornada do colaborador")
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<WorkSessionResponse>> start(
            @PathVariable String collaboratorId) {
        var dto = startUseCase.execute(new StartWorkSessionCommand(collaboratorId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(presenter.toResponse(dto)));
    }

    @Operation(summary = "Encerra a jornada em andamento do colaborador")
    @PostMapping("/finish")
    public ResponseEntity<ApiResponse<WorkSessionResponse>> finish(
            @PathVariable String collaboratorId) {
        var dto = finishUseCase.execute(new FinishWorkSessionCommand(collaboratorId));
        return ResponseEntity.ok(ApiResponse.ok(presenter.toResponse(dto)));
    }

    @Operation(summary = "Histórico paginado de jornadas do colaborador")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkSessionResponse>>> history(
            @PathVariable String collaboratorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageDto = historyUseCase.execute(
                new WorkSessionHistoryQuery(collaboratorId, page, size));
        return ResponseEntity.ok(ApiResponse.ok(presenter.toPageResponse(pageDto)));
    }
}
