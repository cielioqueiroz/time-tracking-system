# Frontend structure (Feature-based + Atomic Design)

```
lib/
├── main.dart                       # composition root → ProviderScope + App
└── app/
    ├── app.dart                    # MaterialApp, theme wiring
    ├── core/                       # cross-cutting infrastructure
    │   ├── constants/              # ApiConstants, etc.
    │   ├── errors/                 # Failure model (sealed)
    │   ├── theme/                  # ThemeMode controller
    │   ├── network/                # dio client + interceptors   (ETAPA 4)
    │   ├── utils/                  # formatters, extensions       (ETAPA 4/5)
    │   └── services/               # app-wide services            (ETAPA 4)
    ├── shared/                     # widgets/helpers shared across features
    ├── design_system/              # ← the single source of UI truth
    │   ├── tokens/                 # spacing, colors, typography, radius,
    │   │                           #   elevation, durations
    │   ├── foundations/            # ThemeData + ThemeExtension
    │   ├── atoms/                  # Button, Input, Badge, Avatar (ETAPA 5)
    │   ├── molecules/              # FormField, ListTile, Card    (ETAPA 5)
    │   ├── organisms/              # CollaboratorCard, Timeline   (ETAPA 5)
    │   ├── templates/              # page scaffolds               (ETAPA 5)
    │   └── layouts/                # responsive layout shells     (ETAPA 5)
    ├── features/
    │   ├── collaborators/
    │   │   ├── data/               # datasources + repository impl (HTTP here only)
    │   │   ├── domain/             # entities + repository interfaces + usecases
    │   │   ├── application/        # Riverpod providers + state controllers
    │   │   └── presentation/       # screens + feature widgets
    │   └── work_sessions/
    │       ├── data/
    │       ├── domain/
    │       ├── application/
    │       └── presentation/
    └── routes/                     # go_router configuration       (ETAPA 4)
```

## Dependency flow (per feature)
`presentation → application → domain ← data`

- **presentation** never imports `data` or `dio`.
- **domain** is pure Dart: entities, repository *interfaces*, usecases.
- **data** implements the domain repository interfaces and is the only place
  that performs HTTP via `dio`.
- **application** exposes Riverpod providers/controllers consumed by the UI.
```
