import '../entities/work_session.dart';

/// Domain contract for work-session operations.
abstract interface class WorkSessionRepository {
  Future<WorkSession> start(String collaboratorId);

  Future<WorkSession> finish(String collaboratorId);

  Future<List<WorkSession>> history(
    String collaboratorId, {
    int page = 0,
    int size = 50,
  });
}
