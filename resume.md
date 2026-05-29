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
- [x] Git — instalado
- [x] **Java 21** — instalado (Temurin 21.0.11)
- [x] **Maven 3.9.9** — instalado em `%LOCALAPPDATA%\Programs\Maven` (adicionado ao PATH do usuário)
- [ ] **Flutter / Dart** — FALTA INSTALAR (necessário p/ rodar frontend — ETAPA 4/5)
- [ ] **Docker + Docker Compose** — FALTA INSTALAR (necessário p/ subir o Postgres)

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

### ETAPA 3 — Backend: apresentação ⬜ PENDENTE
- [ ] Controllers (collaborators, work-sessions, auth)
- [ ] Requests + Bean Validation
- [ ] Responses + presenters
- [ ] Global Exception Handler completo
- [ ] Swagger/OpenAPI documentado
- [ ] JWT authentication
- [ ] Paginação

### ETAPA 4 — Frontend: camadas ⬜ PENDENTE
- [ ] Rotas (go_router)
- [ ] Providers (Riverpod)
- [ ] Services (network/dio + interceptors)
- [ ] Repositories (data + domain)
- [ ] State management (controllers + estados)

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

## ▶️ Próximo passo imediato
**ETAPA 3** (em andamento) — adapters de entrada/saída do backend:
1. Persistência JPA (entidades JPA, Spring Data repositories, mappers, impl dos ports) → faz o app subir
2. Controllers REST + requests (Bean Validation) + presenters/responses
3. Global Exception Handler completo (mapeando `DomainException.code` → HTTP)
4. JWT auth + Swagger documentado + paginação na API

## 📝 Notas / pendências do usuário
- Usuário faz os commits manualmente. **O assistente NUNCA commita** — apenas fornece os comandos.
- Instalar as ferramentas faltantes (Java/Maven/Flutter/Docker) para executar localmente.
