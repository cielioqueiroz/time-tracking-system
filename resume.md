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
| Mapping | MapStruct |
| JWT | jjwt (io.jsonwebtoken) |
| Docs API | springdoc-openapi |
| Pacote raiz | `com.company.timetracking` |
| State mgmt (front) | Riverpod |
| Roteamento (front) | go_router |
| HTTP client (front) | dio |
| Models (front) | freezed + json_serializable |

## ⚙️ Ferramentas do ambiente (status na máquina do usuário)
- [x] Node v24 — instalado
- [x] Git — instalado (repo no GitHub: git@github.com:cielioqueiroz/time-tracking-system.git, branch main)
- [x] **Java 21** — instalado (Temurin 21.0.11)
- [x] **Maven 3.9.9** — instalado em `%LOCALAPPDATA%\Programs\Maven` (no PATH do usuário)
- [x] **Flutter** — clonado em `%LOCALAPPDATA%\flutter` (branch stable, no PATH do usuário). Falta rodar `flutter --version` 1x (baixa Dart SDK) e `flutter doctor`.
- [x] **Docker Desktop** — instalado via winget. Falta ABRIR o app 1x (aceitar WSL2/termos) e confirmar "Engine running". Pode exigir reiniciar o PC.

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

### ETAPA 5 — Frontend: telas e UX ⬜ PENDENTE
- [ ] Collaborators screen (listagem, status, ações, loading/empty/error)
- [ ] Create/Edit screen (form, validação, feedback)
- [ ] History screen (timeline)
- [ ] Responsividade, animações, microinterações, dark/light

### ETAPA 6 — Finalização ⬜ PENDENTE
- [ ] Testes unitários
- [ ] Refatoração + revisão arquitetural
- [ ] Otimização
- [ ] README profissional completo
- [ ] Polish final

---

## ▶️ PONTO DE RETOMADA (pós-reboot do Docker)
O usuário foi abrir o Docker Desktop e possivelmente reiniciar o PC. Ao voltar, fazer NESTA ordem:

1. **Confirmar ferramentas** (no terminal do VS Code do usuário):
   ```powershell
   docker --version ; docker ps ; flutter --version
   ```
   - `docker ps` sem erro = Docker rodando. Se "cannot connect to the Docker daemon" → abrir Docker Desktop e esperar ficar verde.
2. **Subir o Postgres + validar o backend** (o assistente pode rodar isso):
   ```powershell
   # na raiz do projeto
   copy .env.example .env   # se ainda não existir
   docker compose up -d
   cd backend
   mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
   ```
   Testar em http://localhost:8080/swagger-ui.html → login admin/admin → criar/listar colaborador.
3. **ETAPA 5** (telas/UX premium). Rodar o app no Chrome (precisa do backend no ar):
   ```powershell
   docker compose up -d                       # Postgres
   cd backend; $env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\time-tracking.jar   # backend (8080)
   # noutro terminal:
   cd frontend; flutter run -d chrome
   ```

> Estado do código: ETAPAS 1-4 COMPLETAS. Backend validado rodando. Frontend com toda a
> arquitetura/estado/rotas prontos e `flutter analyze` limpo — só faltam as TELAS (ETAPA 5).
> Lembrar de commitar (o usuário faz os commits).

## 📝 Notas / pendências do usuário
- Usuário faz os commits manualmente. **O assistente NUNCA commita** — apenas fornece os comandos.
- Instalar as ferramentas faltantes (Java/Maven/Flutter/Docker) para executar localmente.
