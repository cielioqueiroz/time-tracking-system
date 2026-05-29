import '../../domain/entities/work_session.dart';

/// Maps the work-session JSON payload to the domain [WorkSession].
abstract final class WorkSessionModel {
  static WorkSession fromJson(Map<String, dynamic> json) {
    return WorkSession(
      id: json['id'] as String,
      collaboratorId: json['collaboratorId'] as String,
      status: WorkSessionStatus.fromApi(json['status'] as String),
      startedAt: DateTime.parse(json['startedAt'] as String),
      endedAt: json['endedAt'] == null
          ? null
          : DateTime.parse(json['endedAt'] as String),
      totalMinutes: (json['totalMinutes'] as num?)?.toInt(),
    );
  }
}
