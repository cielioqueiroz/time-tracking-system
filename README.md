# ⏱️ Enterprise Time Tracking System

[![CI](https://github.com/cielioqueiroz/time-tracking-system/actions/workflows/ci.yml/badge.svg)](https://github.com/cielioqueiroz/time-tracking-system/actions/workflows/ci.yml)

Sistema full-stack de **registro de ponto** com qualidade de produção: cadastro de
colaboradores, controle de jornadas (iniciar/encerrar com cálculo automático de
horas), histórico em linha do tempo, **relatório de horas** e **exportação CSV** —
construído sobre **Arquitetura Hexagonal + Clean Architecture** no backend e
**Feature-based + Atomic Design** no frontend.

> Este README é um guia **passo a passo** para clonar e rodar o projeto do zero na
> sua máquina, sem quebrar nada. Se algo der errado, veja a seção
> [🛟 Solução de problemas](#-solução-de-problemas) no fim.

---

## 📑 Sumário
- [Stack](#-stack)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos-instale-antes-de-começar)
- [Passo a passo para rodar](#-passo-a-passo-para-rodar-localmente)
- [Rodando os testes](#-rodando-os-testes)
- [Referência da API](#-referência-da-api)
- [Autenticação](#-autenticação-jwt)
- [Integração contínua (CI)](#-integração-contínua-ci)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Solução de problemas](#-solução-de-problemas)

---

## 🧰 Stack

| Camada | Tecnologias |
|--------|-------------|
| **Backend** | Java 21 · Spring Boot 3.4 · Spring Data JPA · PostgreSQL 16 · Flyway · Spring Security + JWT (`jjwt`) · springdoc-openapi |
| **Frontend** | Flutter · Dart · Riverpod · go_router · dio · google_fonts (Manrope + JetBrains Mono) · modelos imutáveis manuais (sem codegen) |
| **Testes** | JUnit 5 · Mockito · AssertJ · Testcontainers (PostgreSQL) · `flutter_test` |
| **Infra / CI** | Docker · Docker Compose · GitHub Actions |

---

## ✨ Funcionalidades
- Cadastro, edição e exclusão de colaboradores. Campos: **Nome, E-mail e Cargo** (e-mail único, validação de formato).
- **Busca** na lista de colaboradores (por nome, cargo ou e-mail).
- Início e encerramento de jornada, com **cálculo automático de horas** e status do colaborador.
- Histórico de jornadas paginado, em linha do tempo.
- **Relatório de horas** por colaborador (total de jornadas, finalizadas e minutos) — agregado no banco.
- **Exportação das jornadas em CSV**.
- Autenticação **JWT** (stateless), documentação **Swagger/OpenAPI**.

## 📌 Decisões de projeto
- **Exclusão de colaborador:** optou-se por **excluir em cascata** — ao remover um
  colaborador, suas jornadas também são removidas (FK `ON DELETE CASCADE` na tabela
  `work_sessions`, com aviso explícito na confirmação da UI). A alternativa de
  **bloquear** a exclusão quando houvesse jornadas foi descartada para manter o
  fluxo simples e previsível para o gestor.
- **Status do colaborador** (`TRABALHANDO` / `FORA_DA_JORNADA`) é derivado das ações
  de jornada — nunca editado manualmente.
- **Cálculo de horas** é feito no backend, no encerramento da jornada.

---

## 🏛️ Arquitetura

**Backend — Hexagonal / Clean.** Dependências sempre para dentro:
`presentation → application → domain ← infrastructure`.
- `domain`: núcleo puro (entidades ricas, value objects, exceções, **portas**).
- `application`: casos de uso, DTOs, commands/queries, mappers.
- `infrastructure`: adapters de saída (JPA, segurança/JWT, config).
- `presentation`: controllers REST, requests (Bean Validation), presenters, `GlobalExceptionHandler`.

**Frontend — Feature-based + Atomic Design.** Cada feature isola
`data / domain / application / presentation`; Design System em camadas
(`tokens → foundations → atoms → molecules → layouts`). A UI nunca faz HTTP nem
contém regra de negócio.

---

## ✅ Pré-requisitos (instale antes de começar)

| Ferramenta | Versão mínima | Verifique com |
|-----------|----------------|----------------|
| **Git** | qualquer recente | `git --version` |
| **Java JDK** | **21** (ex.: Temurin 21) | `java -version` |
| **Maven** | 3.9+ | `mvn -v` |
| **Flutter SDK** | 3.5+ (traz o Dart 3.5+) | `flutter --version` |
| **Docker Desktop** | recente, **em execução** | `docker --version` e `docker ps` |

> ⚠️ **Windows:** depois de instalar essas ferramentas, **feche e reabra o terminal**
> (ou reinicie o VS Code) para o `PATH` ser atualizado. Se um comando como `mvn`,
> `java` ou `flutter` aparecer como *"não é reconhecido"*, veja
> [Solução de problemas → PATH](#1-comando-não-é-reconhecido-mvn-java-flutter-docker).

Confirme que tudo responde antes de prosseguir:
```bash
git --version
java -version
mvn -v
flutter --version
docker ps        # o Docker Desktop precisa estar aberto/rodando
```

---

## ▶️ Passo a passo para rodar localmente

A aplicação tem **3 peças** que sobem separadamente: **banco (Docker)**,
**backend (Spring Boot)** e **frontend (Flutter web)**. Rode nesta ordem.

### 1. Clonar o repositório
```bash
git clone https://github.com/cielioqueiroz/time-tracking-system.git
cd time-tracking-system
```

### 2. Subir o banco de dados (PostgreSQL via Docker)
O `docker-compose.yml` sobe um PostgreSQL 16 já configurado.

```bash
# 1) copie o arquivo de variáveis de ambiente (não precisa editar para uso local)
cp .env.example .env          # no Windows PowerShell: Copy-Item .env.example .env

# 2) suba o Postgres (Docker Desktop precisa estar rodando)
docker compose up -d

# 3) confirme que está de pé e "healthy"
docker compose ps
```
> O banco fica exposto em `localhost:5432` (db/usuário/senha: `timetracking`).
> Os dados persistem num volume Docker entre reinícios.

### 3. Subir o backend (API Spring Boot)
Em **um terminal** dedicado, a partir da pasta do projeto:

```bash
cd backend
mvn spring-boot:run
```
Ou, gerando o jar e executando (perfil de desenvolvimento):
```bash
cd backend
mvn -B clean package -DskipTests
# bash:
SPRING_PROFILES_ACTIVE=dev java -jar target/time-tracking.jar
# Windows PowerShell:
$env:SPRING_PROFILES_ACTIVE="dev"; java -jar target\time-tracking.jar
```

Quando aparecer **`Started TimeTrackingApplication`**, o backend está no ar:
- API: `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html` (explore e teste a API aqui)
- Health: `http://localhost:8080/actuator/health` (deve responder `{"status":"UP"}`)
- A migração do schema (Flyway) roda **automaticamente** no startup.

> 💡 Esse terminal fica **ocupado** mostrando os logs do backend — isso é normal.
> Não o feche enquanto estiver usando a aplicação. Para parar o backend, use `Ctrl+C`.

### 4. Subir o frontend (Flutter web)
Em **outro terminal** (deixe o backend rodando no primeiro):

```bash
cd frontend
flutter pub get
flutter run -d chrome
```
O Chrome abre sozinho com o app, que **já autentica** com o usuário padrão
(`admin/admin`) e mostra a lista de colaboradores.

> Comandos úteis enquanto o `flutter run` está ativo: **`r`** = hot reload,
> **`R`** = hot restart, **`q`** = sair. (Apertar `q` encerra o app — basta rodar
> `flutter run -d chrome` de novo para voltar.)

#### Apontar o frontend para outra URL de API
Por padrão o frontend chama `http://localhost:8080`. Para mudar (ex.: backend em
outra máquina), use `--dart-define`:
```bash
flutter run -d chrome --dart-define=API_BASE_URL=http://192.168.0.10:8080
```

### (Opcional) Subir backend + banco juntos via Docker
Em vez de rodar o backend pelo Maven, você pode subir tudo em containers:
```bash
docker compose --profile full up -d
```
Isso sobe PostgreSQL **e** o backend (no perfil `prod`). Defina um `JWT_SECRET`
forte no `.env` antes (veja `.env.example`).

---

## 🧪 Rodando os testes

```bash
# Backend — testes unitários (rápidos, NÃO precisam de Docker)
cd backend && mvn test

# Backend — unitários + integração (sobe um PostgreSQL real via Testcontainers)
cd backend && mvn verify

# Frontend
cd frontend && flutter analyze && flutter test
```

> **Sobre o `mvn verify` e o Docker:** os testes de integração (classes `*IT`) usam
> **Testcontainers**, que precisa de um Docker acessível. Se nenhum ambiente Docker
> utilizável for encontrado, esses testes são **pulados automaticamente** (não
> falham) — então `mvn verify` continua **verde** em qualquer máquina. Com Docker
> disponível (ou no CI), eles **rodam de verdade** contra um PostgreSQL real.

---

## 🌐 Referência da API

Base: `/api/v1`

| Método | Rota | Auth | Descrição |
|--------|------|:----:|-----------|
| `POST` | `/auth/login` | — | Autentica e emite o token JWT |
| `GET`  | `/collaborators?page=&size=` | ✅ | Lista colaboradores (paginado) |
| `POST` | `/collaborators` | ✅ | Cria colaborador |
| `GET`  | `/collaborators/{id}` | ✅ | Busca por id |
| `PUT`  | `/collaborators/{id}` | ✅ | Atualiza nome, e-mail e cargo |
| `DELETE` | `/collaborators/{id}` | ✅ | Exclui colaborador (e suas jornadas) |
| `POST` | `/collaborators/{id}/work-sessions/start` | ✅ | Inicia a jornada |
| `POST` | `/collaborators/{id}/work-sessions/finish` | ✅ | Encerra a jornada em andamento |
| `GET`  | `/collaborators/{id}/work-sessions?page=&size=` | ✅ | Histórico (mais recentes primeiro) |
| `GET`  | `/collaborators/{id}/work-sessions/summary?from=&to=` | ✅ | Resumo de horas (período opcional) |
| `GET`  | `/collaborators/{id}/work-sessions/export` | ✅ | Exporta as jornadas em CSV |

### Exemplo de payload (criar/atualizar colaborador)
```json
{ "name": "José Silva", "email": "jose.silva@empresa.com", "cargo": "Desenvolvedor" }
```
Os três campos são obrigatórios. O e-mail é validado e precisa ser único
(case-insensitive); o cargo aceita até 100 caracteres.

### Envelope de resposta
```jsonc
// sucesso
{ "success": true, "data": { ... }, "timestamp": "2026-05-29T12:00:00Z" }
// erro
{ "success": false, "errors": [{ "code": "EMAIL_ALREADY_EXISTS", "message": "...", "field": null }],
  "timestamp": "2026-05-29T12:00:00Z" }
```

### Regras de negócio × HTTP
| Regra | Resultado |
|-------|-----------|
| E-mail único (case-insensitive) | `409 Conflict` |
| Uma jornada ativa por colaborador | `409 Conflict` |
| Encerrar sem jornada em andamento | `404 Not Found` |
| Validação de payload | `400 Bad Request` (erros por campo) |
| Sem token / token inválido | `401 Unauthorized` |

---

## 🔐 Autenticação (JWT)

A API é **stateless**:
1. `POST /api/v1/auth/login` com `{ "username": "admin", "password": "admin" }` → retorna um token Bearer.
2. As demais rotas exigem o header `Authorization: Bearer <token>`.
3. Rotas públicas: login, Swagger/OpenAPI e `/actuator/health`.

> Usuário padrão de desenvolvimento: **`admin` / `admin`** (configurável por variáveis
> de ambiente — veja `.env.example`). O frontend já faz esse login sozinho no start.

---

## 🔄 Integração contínua (CI)

O workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) roda a cada push/PR na `main`:
- **Backend:** `mvn verify` (unitários + integração com PostgreSQL real via Testcontainers).
- **Frontend:** `flutter analyze` + `flutter test`.

O status aparece no badge no topo deste README e na aba **Actions** do GitHub.

---

## 📁 Estrutura do projeto

```
.
├── backend/                # API Spring Boot
│   ├── src/main/java/com/company/timetracking/
│   │   ├── domain/         # entidades, VOs, enums, exceções, portas
│   │   ├── application/    # use cases, DTOs, commands, queries, mappers
│   │   ├── infrastructure/ # adapters JPA, security/JWT, config
│   │   └── presentation/   # controllers, requests, responses, presenters, handlers
│   ├── src/test/java/...   # testes unitários e de integração (*IT)
│   └── src/main/resources/ # application.yml + migrations Flyway
├── frontend/               # App Flutter (web)
│   ├── lib/app/...         # core, design_system, features, routes
│   └── test/               # testes
├── .github/workflows/ci.yml
├── docker-compose.yml      # PostgreSQL (+ backend no profile `full`)
└── .env.example
```

---

## 🛟 Solução de problemas

### 1. Comando *"não é reconhecido"* (`mvn`, `java`, `flutter`, `docker`)
O `PATH` do terminal está desatualizado (comum no Windows logo após instalar as ferramentas).
- **Solução simples:** feche o terminal e abra um novo (ou reinicie o VS Code).
- **Solução na hora (PowerShell):** cole esta linha e rode os comandos de novo:
  ```powershell
  $env:Path = [Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [Environment]::GetEnvironmentVariable("Path","User")
  ```

### 2. `docker: ... not recognized` ou erro de conexão
O **Docker Desktop não está aberto/rodando**. Abra o Docker Desktop, espere ficar
"Engine running" e rode `docker ps` para confirmar.

### 3. Porta já em uso (`5432` ou `8080`)
Algo já está usando a porta.
- Postgres (5432): pare outro Postgres local, ou mude `DB_PORT` no `.env`.
- Backend (8080): pare o processo na porta, ou rode com `SERVER_PORT=8081`.

### 4. `Cannot find path ...\backend\backend`
Você rodou `cd backend` estando **já dentro** de `backend`. Confirme onde está com
`pwd` e ajuste (rode os comandos do backend a partir da pasta `backend`).

### 5. Falha ao gerar o jar: *"Unable to rename ... time-tracking.jar"*
Há um **backend ainda rodando** segurando o arquivo. Pare o processo Java
(feche o terminal do backend ou `Ctrl+C`) e rode o `mvn package` de novo.

### 6. `mvn verify` "pula" os testes de integração
É o comportamento esperado quando **não há Docker utilizável**. Com o Docker Desktop
aberto e funcional, eles rodam. O build fica verde nos dois casos.

### 7. Frontend não conecta na API / erro de rede
- O backend está rodando em `http://localhost:8080`? (cheque o `/actuator/health`).
- Rodando o front em outra origem? Use `--dart-define=API_BASE_URL=...` (seção acima).

---

> Dúvidas ou problemas para rodar? Abra uma issue no repositório.
