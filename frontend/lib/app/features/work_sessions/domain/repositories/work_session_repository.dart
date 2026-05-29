import '../entities/work_session.dart';
import '../entities/work_summary.dart';

abstract interface class WorkSessionRepository {
  Future<WorkSession> start(String collaboratorId);

  Future<WorkSession> finish(String collaboratorId);

  Future<List<WorkSession>> history(
    String collaboratorId, {
    int page = 0,
    int size = 50,
  });

  Future<WorkSummary> summary(String collaboratorId);

  Future<String> exportCsv(String collaboratorId);
}
