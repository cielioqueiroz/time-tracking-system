# ⏱️ Enterprise Time Tracking System

Sistema full-stack de **registro de ponto** com qualidade de produção: cadastro de
colaboradores, controle de jornadas (iniciar / encerrar com cálculo automático de
horas) e histórico paginado — construído sobre **Arquitetura Hexagonal + Clean
Architecture** no backend e **Feature-based + Atomic Design** no frontend.

O projeto prioriza separação de responsabilidades, domínio rico com invariantes,
tratamento de erros consistente e uma camada de UI premium e responsiva.

---

## 🧰 Stack

| Camada | Tecnologias |
|--------|-------------|
| **Backend** | Java 21 · Spring Boot 3.4 · Spring Data JPA · PostgreSQL 16 · Flyway · Spring Security + JWT (`jjwt`) · springdoc-openapi |
| **Frontend** | Flutter · Dart · Riverpod · go_router · dio · google_fonts (Manrope + JetBrains Mono) · modelos imutáveis manuais (sem codegen) |
| **Testes** | JUnit 5 · Mockito · AssertJ · H2 (backend) · `flutter_test` (frontend) |
| **Infra** | Docker · Docker Compose |

---

## 🏛️ Arquitetura

### Backend — Hexagonal / Clean

Fluxo de dependências sempre para dentro: `presentation → application → domain ← infrastructure`.

- **`domain`** — núcleo puro, sem framework. Entidades ricas (`Collaborator`,
  `WorkSession`) que protegem as próprias invariantes, value objects (`Email`,
  `WorkPeriod`, `CollaboratorId`, `WorkSessionId`), enums, exceções de domínio e as
  **portas** (interfaces de repositório).
- **`application`** — casos de uso (um por operação), DTOs, commands, queries e
  mappers. Orquestra o domínio; não conhece HTTP nem JPA.
- **`infrastructure`** — adapters de saída: implementações JPA das portas, segurança
  (filtro JWT, entry point), configuração (clock, CORS, OpenAPI).
- **`presentation`** — adapters de entrada: controllers REST finos, requests com Bean
  Validation, presenters (DTO → view model) e o `GlobalExceptionHandler`.

> A inversão de dependência mantém o domínio testável de forma isolada e o torna
> imune a trocas de framework/banco.

### Frontend — Feature-based + Atomic Design

Cada feature (`auth`, `collaborators`, `work_sessions`) isola `data / domain /
application / presentation`. O **Design System** é centralizado em camadas
(`tokens → foundations → atoms → molecules → layouts`). A UI nunca faz HTTP nem
contém regra de negócio — repositórios traduzem erros de transporte em `Failure`s
e os providers Riverpod expõem o estado para a tela.

---

## 📁 Estrutura

```
.
├── backend/                # API Spring Boot (pacotes documentados em package-info.java)
│   ├── src/main/java/com/company/timetracking/
│   │   ├── domain/         # entidades, VOs, enums, exceções, portas
│   │   ├── application/    # use cases, DTOs, commands, queries, mappers
│   │   ├── infrastructure/ # adapters JPA, security/JWT, config
│   │   └── presentation/   # controllers, requests, responses, presenters, handlers
│   └── src/test/java/...   # testes de domínio e de casos de uso
├── frontend/               # App Flutter (estrutura em frontend/lib/STRUCTURE.md)
│   ├── lib/app/...
│   └── test/               # testes unitários e de widget
├── docker-compose.yml      # PostgreSQL (+ backend no profile `full`)
└── .env.example
```

---

## ▶️ Como executar

### Pré-requisitos
- **Java 21+** e **Maven 3.9+**
- **Flutter SDK 3.4+**
- **Docker** + **Docker Compose**

### 1. Banco de dados
```bash
cp .env.example .env          # ajuste segredos se desejar
docker compose up -d          # sobe o PostgreSQL (porta 5432)
```

### 2. Backend
```bash
cd backend
mvn spring-boot:run           # http://localhost:8080
# Swagger UI:  http://localhost:8080/swagger-ui.html
# Health:      http://localhost:8080/actuator/health
```

> No Windows (PowerShell), para gerar o jar e rodar:
> ```powershell
> mvn -B clean package -DskipTests
> $env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\time-tracking.jar
> ```

### 3. Frontend
```bash
cd frontend
flutter pub get
flutter run -d chrome         # autentica sozinho (admin/admin) no bootstrap
```

### Tudo em containers (opcional)
```bash
docker compose --profile full up -d   # PostgreSQL + backend
```

---

## 🔐 Autenticação (JWT)

A API é **stateless**. O fluxo:

1. `POST /api/v1/auth/login` com `{ "username": "admin", "password": "admin" }`
   retorna um token Bearer.
2. As demais rotas exigem o header `Authorization: Bearer <token>`.
3. Rotas públicas: login, Swagger/OpenAPI e `/actuator/health`. Qualquer outra
   requisição sem token válido recebe **401** em JSON (via entry point dedicado).

> Usuário padrão de desenvolvimento: **`admin` / `admin`**.

---

## 🌐 Referência de endpoints

Base: `/api/v1`

| Método | Rota | Auth | Descrição |
|--------|------|:----:|-----------|
| `POST` | `/auth/login` | — | Autentica e emite o token JWT |
| `GET`  | `/collaborators?page=&size=` | ✅ | Lista colaboradores (paginado) |
| `POST` | `/collaborators` | ✅ | Cria colaborador |
| `GET`  | `/collaborators/{id}` | ✅ | Busca colaborador por id |
| `PUT`  | `/collaborators/{id}` | ✅ | Atualiza nome/e-mail |
| `DELETE` | `/collaborators/{id}` | ✅ | Exclui colaborador (e suas jornadas) |
| `POST` | `/collaborators/{id}/work-sessions/start` | ✅ | Inicia a jornada |
| `POST` | `/collaborators/{id}/work-sessions/finish` | ✅ | Encerra a jornada em andamento |
| `GET`  | `/collaborators/{id}/work-sessions?page=&size=` | ✅ | Histórico de jornadas (mais recentes primeiro) |

### Envelope de resposta

Toda resposta usa um envelope previsível:

```jsonc
// sucesso
{ "success": true, "data": { ... }, "timestamp": "2026-05-29T12:00:00Z" }

// erro
{
  "success": false,
  "errors": [{ "code": "EMAIL_ALREADY_EXISTS", "message": "...", "field": null }],
  "timestamp": "2026-05-29T12:00:00Z"
}
```

---

## 📐 Regras de negócio (e o HTTP correspondente)

| Regra | Resultado |
|-------|-----------|
| E-mail único (case-insensitive) | `409 Conflict` ao duplicar |
| Uma jornada ativa por colaborador | `409 Conflict` ao iniciar a segunda |
| Encerrar sem jornada em andamento | `404 Not Found` |
| Encerrar jornada já encerrada | bloqueado na entidade (guard de domínio) |
| Cálculo de horas | `totalMinutes` derivado automaticamente ao encerrar |
| Status do colaborador | alterna `TRABALHANDO` / `FORA_DA_JORNADA` automaticamente |
| Validação de payload | `400 Bad Request` com erros por campo |
| Sem token / token inválido | `401 Unauthorized` |

---

## 🧪 Testes

```bash
# Backend (JUnit 5 + Mockito + AssertJ)
cd backend && mvn test

# Frontend (flutter_test)
cd frontend && flutter analyze && flutter test
```

Cobertura atual:
- **Backend:** entidades, value objects e os 8 casos de uso (criação, atualização,
  exclusão, consulta, listagem e início/encerramento/histórico de jornada),
  incluindo os caminhos de erro de cada regra de negócio.
- **Frontend:** serialização dos models, formatters de data/hora/duração, mapeamento
  de erros de rede (`DioException → Failure`) e widgets do Design System.

---

## 🚀 Melhorias futuras

- Perfis de usuário e papéis (RBAC) além do admin único.
- Edição/correção manual de jornadas com trilha de auditoria.
- Relatórios e exportação (CSV/PDF) por período.
- Testes de integração end-to-end (Testcontainers no backend; integração no front).
- Pipeline CI/CD (build, testes e análise estática automatizados).

---

## 📝 Continuidade

O progresso detalhado por etapa está em [`resume.md`](resume.md).
Decisões técnicas e estrutura de pastas do frontend em
[`frontend/lib/STRUCTURE.md`](frontend/lib/STRUCTURE.md).
