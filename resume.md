# 📋 RESUME — Enterprise Time Tracking System

> **Arquivo de continuidade.** Sempre que reabrir o projeto ou os tokens acabarem,
> peça para o assistente **ler este `resume.md`** e ele continua exatamente de onde parou.
> As tarefas concluídas ficam marcadas com `[x]`; as pendentes com `[ ]`.

---

## 🎯 Resumo do projeto
Sistema de Registro de Ponto (Time Tracking) full-stack enterprise.
- **Backend:** Java 21 · Spring Boot 3 · PostgreSQL · Arquitetura Hexagonal + Clean Architecture
- **Frontend:** Flutter · Dart · Riverpod · Feature-based + Atomic Design
- **Infra:** Docker Compose · Flyway · Swagger/OpenAPI · JWT

## 🧱 Decisões técnicas tomadas
| Tema | Decisão |
|------|---------|
| Build backend | Maven |
| Migrations | Flyway |
| Mapping | mappers manuais (`@Component`) — MapStruct/Lombok removidos por não uso na ETAPA 6 |
| JWT | jjwt (io.jsonwebtoken) |
| Docs API | springdoc-openapi |
| Pacote raiz | `com.company.timetracking` |
| State mgmt (front) | Riverpod |
| Roteamento (front) | go_router |
| HTTP client (front) | dio |
| Models (front) | modelos imutáveis manuais (sem codegen) |
| Fontes (front) | Manrope (UI) + JetBrains Mono (tempos) via google_fonts |

## ⚙️ Ferramentas do ambiente (status na máquina do usuário)
- [x] Node v24 — instalado
- [x] Git — instalado (repo no GitHub: git@github.com:cielioqueiroz/time-tracking-system.git, branch main)
- [x] **Java 21** — instalado (Temurin 21.0.11)
- [x] **Maven 3.9.9** — instalado em `%LOCALAPPDATA%\Programs\Maven` (no PATH do usuário)
- [x] **Flutter 3.44 / Dart 3.12** — funcionando (web habilitado, build web OK)
- [x] **Docker Desktop** — funcionando ("Engine running"); Postgres já validado via compose

> 💡 Meu shell (assistente) NÃO herda o PATH atualizado automaticamente. Para usar mvn/flutter/docker
> nas execuções, prefixar com:
> `$env:Path = [Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [Environment]::GetEnvironmentVariable("Path","User")`

---

## ✅ Progresso por etapa

### ETAPA 1 — Estrutura base  ✅ CONCLUÍDA
- [x] `.gitignore`
- [x] `resume.md` (este arquivo)
- [x] `README.md` (skeleton)
- [x] Backend: `pom.xml`
- [x] Backend: classe `TimeTrackingApplication`
- [x] Backend: `application.yml` + profiles `dev`/`prod`
- [x] Backend: estrutura de pacotes (domain/application/infrastructure/presentation) com package-info
- [x] Backend: migration inicial Flyway (`V1__init_schema.sql` — collaborators, work_sessions, constraints, índices)
- [x] Backend: response pattern (`ApiResponse`, `ApiError`)
- [x] Backend: `GlobalExceptionHandler` (skeleton)
- [x] Backend: `OpenApiConfig`
- [x] Backend: `SecurityConfig` (skeleton — permitAll, JWT entra na ETAPA 3)
- [x] Backend: `Dockerfile` + `.dockerignore`
- [x] Frontend: `pubspec.yaml`
- [x] Frontend: `main.dart` + `app.dart`
- [x] Frontend: estrutura de pastas (documentada em `frontend/lib/STRUCTURE.md`)
- [x] Frontend: design system base (tokens: spacing, color, typography, radius, elevation, durations)
- [x] Frontend: theme light/dark (`AppTheme` + `AppThemeExt`)
- [x] `docker-compose.yml` + `.env.example`
- [x] **SEM implementar regras de negócio ainda** (conforme master prompt) ✓

### ETAPA 2 — Backend: domínio ✅ CONCLUÍDA (compila: BUILD SUCCESS)
- [x] Entidades ricas: `Collaborator`, `WorkSession` (com comportamento + invariantes)
- [x] Enums: `WorkSessionStatus` (EM_ANDAMENTO/FINALIZADA), `CollaboratorStatus` (TRABALHANDO/FORA_DA_JORNADA)
- [x] Value Objects: `Email`, `WorkPeriod` (cálculo de horas), `CollaboratorId`, `WorkSessionId`
- [x] Repository ports: `CollaboratorRepository`, `WorkSessionRepository` + paginação de domínio (`PageQuery`, `Page`)
- [x] Exceptions de domínio (base `DomainException` com `code` + 7 subclasses)
- [x] Use cases colaborador: Create, Update, Delete, Get, List
- [x] Use cases jornada: Start, Finish, History
- [x] DTOs, commands, queries, mappers (camada application)
- [x] `Clock` injetável (testabilidade)
- [x] Regras: e-mail único (case-insensitive), jornada única, encerrar inexistente, encerrar já encerrada (guard na entidade), cálculo automático de horas, status automático

> ⚠️ App ainda NÃO sobe (faltam os adapters JPA que implementam os ports — ETAPA 3).
> Verificação desta etapa: `cd backend; mvn clean compile`.

### ETAPA 3 — Backend: apresentação + persistência ✅ CONCLUÍDA (compila: BUILD SUCCESS)
- [x] Persistência JPA: entidades JPA, Spring Data repos, mappers JPA↔domínio, adapters dos ports
- [x] Controllers: `CollaboratorController`, `WorkSessionController`, `AuthController`
- [x] Requests + Bean Validation (`@NotBlank`, `@Email`, `@Size`)
- [x] Responses + presenters (DTO → view model)
- [x] Global Exception Handler completo (DomainException.code → HTTP status, validação, 401, 500)
- [x] Swagger/OpenAPI documentado (+ esquema Bearer JWT)
- [x] JWT authentication (login admin/admin, filtro, entry point 401 JSON)
- [x] Paginação ponta a ponta (`page`/`size` → `PageResponse`)

> Endpoints:
> - `POST /api/v1/auth/login` (público) → retorna token
> - `GET/POST/PUT/DELETE /api/v1/collaborators` (autenticado)
> - `POST /api/v1/collaborators/{id}/work-sessions/start|finish` + `GET .../work-sessions` (autenticado)
> - Swagger UI: `http://localhost:8080/swagger-ui.html`
> - Usuário padrão: **admin / admin**

### VALIDAÇÃO DO BACKEND ✅ CONCLUÍDA (rodou de verdade em 2026-05-28)
- [x] Docker Desktop rodando + Postgres (`postgres:16-alpine`) healthy via compose
- [x] Backend subiu (`{"status":"UP"}`), Flyway migrou o schema
- [x] Fluxo completo testado: login → criar/listar colaborador → iniciar/encerrar jornada → histórico
- [x] Regras validadas (HTTP correto): e-mail duplicado=409, encerrar sem jornada=404, jornada dupla=409, sem token=401, validação=400
- [x] Cálculo de horas funcionando (totalMinutes preenchido ao encerrar)

> Como rodar de novo:
> ```powershell
> docker compose up -d                 # Postgres (já tem volume com dados)
> cd backend
> mvn -B clean package -DskipTests
> $env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\time-tracking.jar
> # Swagger: http://localhost:8080/swagger-ui.html  (admin/admin)
> ```

### ETAPA 4 — Frontend: camadas ✅ CONCLUÍDA (flutter analyze: No issues found!)
- [x] Core network: `dio_provider` (interceptor de token), `error_mapper` (DioException→Failure), `auth_session_controller`
- [x] Feature auth: datasource + repo + `sessionBootstrapProvider` (login admin/admin no start)
- [x] Feature collaborators: domain (entity+repo), data (model+datasource+repo impl), application (providers + `CollaboratorsController` AsyncNotifier com create/edit/delete/refresh)
- [x] Feature work_sessions: domain (entity+repo), data (model+datasource+repo impl), application (providers + `workSessionHistoryProvider` + `WorkSessionActions` start/finish)
- [x] Rotas (go_router): lista, novo, editar, histórico (`app_router` + `app_routes`)
- [x] `app.dart` religado: MaterialApp.router + gate de bootstrap (splash/erro+retry)
- [x] pubspec enxuto (removido codegen; modelos imutáveis manuais)

> ⚠️ Telas ainda são PLACEHOLDERS (Scaffold "em construção"). A UI premium é a ETAPA 5.
> Verificação: `cd frontend; flutter analyze`.

### ETAPA 5 — Frontend: telas e UX ✅ CONCLUÍDA (analyze limpo, 3 testes passam, build web OK)
- [x] Tipografia premium: Manrope (UI) + JetBrains Mono (tempos) via google_fonts
- [x] Design System: átomos (AppCard, AppButton, AppTextField, StatusBadge c/ dot pulsante, AppAvatar),
      moléculas (AppSkeleton shimmer, EmptyState, ErrorState, ConfirmDialog, AppFeedback snackbars),
      layout (AppPage responsivo), shared (ThemeToggleButton, FadeSlideIn)
- [x] Collaborators screen: lista com avatar/status/ações, iniciar/encerrar jornada, editar, excluir
      (confirm dialog), histórico, loading skeletons, empty/error states, pull-to-refresh, stagger
- [x] Create/Edit screen: validação client + mapeamento de erros de campo do servidor, feedback
- [x] History screen: timeline elegante (rail + dot, entrada/saída/duração em mono, badge de status)
- [x] Dark/light mode (toggle), responsividade (largura máx), animações leves
- [x] Plataforma web adicionada (`flutter create . --platforms web`) + `widget_test.dart` real (DS)

> Verificação: `cd frontend; flutter analyze; flutter test; flutter build web`

### ETAPA 6 — Finalização ✅ CONCLUÍDA (backend: 34 testes; frontend: 25 testes; analyze limpo)
- [x] Testes unitários backend: os 8 use cases cobertos (Create/Update/Delete/Get/List + Start/Finish/History),
      com caminhos de erro de cada regra (16 → 34 testes; BUILD SUCCESS)
- [x] Testes unitários frontend: models (serialização), formatters, error_mapper (DioException→Failure) (3 → 25 testes)
- [x] Revisão arquitetural: camadas validadas (hexagonal/clean aderente); ajuste de estilo no
      `CollaboratorRepositoryAdapter` (import de `Optional`)
- [x] Otimização: removido peso morto do `pom.xml` (MapStruct + Lombok não eram importados em lugar algum;
      annotation processors e plugin de compilação enxugados)
- [x] README profissional completo (visão, arquitetura, estrutura, como rodar, auth JWT, endpoints,
      envelope, regras×HTTP, testes, futuro) — corrigido para refletir a stack real (sem MapStruct/freezed)
- [x] Polish final + este `resume.md` atualizado

> Verificação: `cd backend; mvn test`  ·  `cd frontend; flutter analyze; flutter test`

---

## ▶️ PONTO DE RETOMADA
Estado do código: **ETAPAS 1-6 COMPLETAS** 🎉. Backend validado rodando, 34 testes passando
(BUILD SUCCESS) e `pom.xml` enxuto. Frontend com UI premium, `flutter analyze` limpo, 25 testes
passando e `flutter build web` OK. README profissional completo.

**Projeto essencialmente finalizado.** Próximos passos opcionais (não obrigatórios), caso queira evoluir:
- Testes de integração end-to-end (Testcontainers no backend).
- RBAC/usuários além do admin único; edição de jornadas com auditoria; relatórios/export.
- Pipeline CI/CD (build + testes + análise estática).

**Rodar o app completo (visual) no Chrome:**
```powershell
docker compose up -d                                                    # Postgres
cd backend; mvn -B clean package -DskipTests; $env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\time-tracking.jar
# noutro terminal:
cd frontend; flutter run -d chrome                                      # autentica sozinho (admin/admin)
```

> Lembrar de commitar (o usuário faz os commits).

## 📝 Notas / pendências do usuário
- Usuário faz os commits manualmente. **O assistente NUNCA commita** — apenas fornece os comandos.
- Instalar as ferramentas faltantes (Java/Maven/Flutter/Docker) para executar localmente.
