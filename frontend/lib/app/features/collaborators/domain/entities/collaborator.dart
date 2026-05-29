enum CollaboratorStatus {
  trabalhando,
  foraDaJornada;

  static CollaboratorStatus fromApi(String value) => switch (value) {
        'TRABALHANDO' => CollaboratorStatus.trabalhando,
        _ => CollaboratorStatus.foraDaJornada,
      };

  bool get isWorking => this == CollaboratorStatus.trabalhando;
}

class Collaborator {
  const Collaborator({
    required this.id,
    required this.name,
    required this.email,
    required this.cargo,
    required this.status,
  });

  final String id;
  final String name;
  final String email;
  final String cargo;
  final CollaboratorStatus status;
}
