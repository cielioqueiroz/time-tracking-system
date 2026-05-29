import '../entities/work_session.dart';
import '../entities/work_summary.dart';

/// Domain contract for work-session operations.
abstract interface class WorkSessionRepository {
  Future<WorkSession> start(String collaboratorId);

  Future<WorkSession> finish(String collaboratorId);

  Future<List<WorkSession>> history(
    String collaboratorId, {
    int page = 0,
    int size = 50,
  });

  Future<WorkSummary> summary(String collaboratorId);

  /// Raw CSV export of all the collaborator's sessions.
  Future<String> exportCsv(String collaboratorId);
}
