import '../../../../core/network/error_mapper.dart';
import '../../domain/entities/work_session.dart';
import '../../domain/entities/work_summary.dart';
import '../../domain/repositories/work_session_repository.dart';
import '../datasources/work_session_remote_datasource.dart';

/// Implements the domain repository over the remote data source.
class WorkSessionRepositoryImpl implements WorkSessionRepository {
  WorkSessionRepositoryImpl(this._remote);

  final WorkSessionRemoteDataSource _remote;

  @override
  Future<WorkSession> start(String collaboratorId) =>
      _guard(() => _remote.start(collaboratorId));

  @override
  Future<WorkSession> finish(String collaboratorId) =>
      _guard(() => _remote.finish(collaboratorId));

  @override
  Future<List<WorkSession>> history(
    String collaboratorId, {
    int page = 0,
    int size = 50,
  }) =>
      _guard(() => _remote.history(collaboratorId, page: page, size: size));

  @override
  Future<WorkSummary> summary(String collaboratorId) =>
      _guard(() => _remote.summary(collaboratorId));

  @override
  Future<String> exportCsv(String collaboratorId) =>
      _guard(() => _remote.exportCsv(collaboratorId));

  Future<T> _guard<T>(Future<T> Function() action) async {
    try {
      return await action();
    } catch (e) {
      throw mapError(e);
    }
  }
}
