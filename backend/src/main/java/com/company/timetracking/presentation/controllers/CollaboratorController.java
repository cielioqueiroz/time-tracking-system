package com.company.timetracking.presentation.controllers;

import com.company.timetracking.application.commands.CreateCollaboratorCommand;
import com.company.timetracking.application.commands.UpdateCollaboratorCommand;
import com.company.timetracking.application.queries.ListCollaboratorsQuery;
import com.company.timetracking.application.usecases.collaborator.CreateCollaboratorUseCase;
import com.company.timetracking.application.usecases.collaborator.DeleteCollaboratorUseCase;
import com.company.timetracking.application.usecases.collaborator.GetCollaboratorUseCase;
import com.company.timetracking.application.usecases.collaborator.ListCollaboratorsUseCase;
import com.company.timetracking.application.usecases.collaborator.UpdateCollaboratorUseCase;
import com.company.timetracking.presentation.presenters.CollaboratorPresenter;
import com.company.timetracking.presentation.requests.CreateCollaboratorRequest;
import com.company.timetracking.presentation.requests.UpdateCollaboratorRequest;
import com.company.timetracking.presentation.responses.ApiResponse;
import com.company.timetracking.presentation.responses.CollaboratorResponse;
import com.company.timetracking.presentation.responses.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for collaborators. Thin layer: validates input, delegates to
 * use cases, presents results in the standardized envelope.
 */
@RestController
@RequestMapping("/api/v1/collaborators")
@Tag(name = "Collaborators", description = "Cadastro e consulta de colaboradores")
public class CollaboratorController {

    private final CreateCollaboratorUseCase createUseCase;
    private final UpdateCollaboratorUseCase updateUseCase;
    private final DeleteCollaboratorUseCase deleteUseCase;
    private final GetCollaboratorUseCase getUseCase;
    private final ListCollaboratorsUseCase listUseCase;
    private final CollaboratorPresenter presenter;

    public CollaboratorController(CreateCollaboratorUseCase createUseCase,
                                  UpdateCollaboratorUseCase updateUseCase,
                                  DeleteCollaboratorUseCase deleteUseCase,
                                  GetCollaboratorUseCase getUseCase,
                                  ListCollaboratorsUseCase listUseCase,
                                  CollaboratorPresenter presenter) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
        this.presenter = presenter;
    }

    @Operation(summary = "Cria um novo colaborador")
    @PostMapping
    public ResponseEntity<ApiResponse<CollaboratorResponse>> create(
            @Valid @RequestBody CreateCollaboratorRequest request) {
        var dto = createUseCase.execute(
                new CreateCollaboratorCommand(request.name(), request.email()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(presenter.toResponse(dto)));
    }

    @Operation(summary = "Lista colaboradores paginados")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CollaboratorResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageDto = listUseCase.execute(new ListCollaboratorsQuery(page, size));
        return ResponseEntity.ok(ApiResponse.ok(presenter.toPageResponse(pageDto)));
    }

    @Operation(summary = "Busca um colaborador por id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CollaboratorResponse>> get(@PathVariable String id) {
        var dto = getUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.ok(presenter.toResponse(dto)));
    }

    @Operation(summary = "Atualiza um colaborador")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CollaboratorResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateCollaboratorRequest request) {
        var dto = updateUseCase.execute(
                new UpdateCollaboratorCommand(id, request.name(), request.email()));
        return ResponseEntity.ok(ApiResponse.ok(presenter.toResponse(dto)));
    }

    @Operation(summary = "Exclui um colaborador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
