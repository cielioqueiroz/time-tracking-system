# ⏱️ Enterprise Time Tracking System

Sistema full-stack de registro de ponto com qualidade de produção: cadastro de
colaboradores, controle de jornadas (iniciar/encerrar) e histórico — construído
sobre **Arquitetura Hexagonal + Clean Architecture** no backend e
**Feature-based + Atomic Design** no frontend.

> 🚧 **Status:** em construção, por etapas. Veja [`resume.md`](resume.md) para o
> progresso detalhado.

## 🧰 Stack
| Camada | Tecnologias |
|--------|-------------|
| Backend | Java 21 · Spring Boot 3 · PostgreSQL · Flyway · MapStruct · JWT · springdoc-openapi |
| Frontend | Flutter · Dart · Riverpod · go_router · dio · freezed |
| Infra | Docker · Docker Compose |

## 🏛️ Arquitetura

**Backend (Hexagonal / Clean):** `presentation → application → domain ← infrastructure`.
O domínio é puro (sem framework); os adapters de entrada (REST) e saída (JPA,
security) dependem para dentro. Portas (interfaces) ficam no `domain`, implementadas
na `infrastructure`.

**Frontend (Feature-based + Atomic Design):** cada feature isola `data / domain /
application / presentation`. Design System centralizado (`tokens → foundations →
atoms → molecules → organisms → templates → layouts`). A UI nunca faz HTTP nem
contém regra de negócio.

## 📁 Estrutura
- `backend/` — API Spring Boot. Estrutura de pacotes documentada em cada `package-info.java`.
- `frontend/` — App Flutter. Estrutura documentada em [`frontend/lib/STRUCTURE.md`](frontend/lib/STRUCTURE.md).
- `docker-compose.yml` — PostgreSQL (+ backend no profile `full`).

## ▶️ Como executar (em construção)

### Pré-requisitos
- Java 21+, Maven (ou o wrapper `mvnw`)
- Flutter SDK 3.5+
- Docker + Docker Compose

### Banco de dados
```bash
cp .env.example .env
docker compose up -d            # sobe o PostgreSQL
```

### Backend
```bash
cd backend
mvn spring-boot:run             # http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Frontend
```bash
cd frontend
flutter pub get
flutter run
```

## 📌 Seções a completar (ETAPA 6)
- [ ] Decisões técnicas detalhadas
- [ ] Screenshots
- [ ] Referência de endpoints
- [ ] Autenticação (fluxo JWT)
- [ ] Melhorias futuras
