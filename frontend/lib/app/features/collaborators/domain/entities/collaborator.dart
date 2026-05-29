/// Working status of a collaborator (mirrors the backend enum).
enum CollaboratorStatus {
  trabalhando,
  foraDaJornada;

  static CollaboratorStatus fromApi(String value) => switch (value) {
        'TRABALHANDO' => CollaboratorStatus.trabalhando,
        _ => CollaboratorStatus.foraDaJornada,
      };

  bool get isWorking => this == CollaboratorStatus.trabalhando;
}

/// Domain entity representing a collaborator. Pure Dart, immutable.
class Collaborator {
  const Collaborator({
    required this.id,
    required this.name,
    required this.email,
    required this.status,
  });

  final String id;
  final String name;
  final String email;
  final CollaboratorStatus status;
}
