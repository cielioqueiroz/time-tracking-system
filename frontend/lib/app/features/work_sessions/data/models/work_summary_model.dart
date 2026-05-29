import '../../domain/entities/work_summary.dart';

abstract final class WorkSummaryModel {
  static WorkSummary fromJson(Map<String, dynamic> json) {
    return WorkSummary(
      totalSessions: (json['totalSessions'] as num).toInt(),
      finishedSessions: (json['finishedSessions'] as num).toInt(),
      totalMinutes: (json['totalMinutes'] as num).toInt(),
    );
  }
}
