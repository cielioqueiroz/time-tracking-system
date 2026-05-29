/// Lifecycle state of a work session (mirrors the backend enum).
enum WorkSessionStatus {
  emAndamento,
  finalizada;

  static WorkSessionStatus fromApi(String value) => switch (value) {
        'EM_ANDAMENTO' => WorkSessionStatus.emAndamento,
        _ => WorkSessionStatus.finalizada,
      };

  bool get isOpen => this == WorkSessionStatus.emAndamento;
}

/// Domain entity for a single journey. Pure Dart, immutable.
class WorkSession {
  const WorkSession({
    required this.id,
    required this.collaboratorId,
    required this.status,
    required this.startedAt,
    this.endedAt,
    this.totalMinutes,
  });

  final String id;
  final String collaboratorId;
  final WorkSessionStatus status;
  final DateTime startedAt;
  final DateTime? endedAt;
  final int? totalMinutes;
}
