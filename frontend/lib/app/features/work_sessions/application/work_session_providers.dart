import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/network/dio_provider.dart';
import '../data/datasources/work_session_remote_datasource.dart';
import '../data/repositories/work_session_repository_impl.dart';
import '../domain/entities/work_session.dart';
import '../domain/entities/work_summary.dart';
import '../domain/repositories/work_session_repository.dart';

final _workSessionDataSourceProvider = Provider<WorkSessionRemoteDataSource>(
  (ref) => WorkSessionRemoteDataSource(ref.watch(dioProvider)),
);

final workSessionRepositoryProvider = Provider<WorkSessionRepository>(
  (ref) => WorkSessionRepositoryImpl(ref.watch(_workSessionDataSourceProvider)),
);

/// Paginated history of a collaborator's sessions, keyed by collaborator id.
final workSessionHistoryProvider =
    FutureProvider.family<List<WorkSession>, String>(
  (ref, collaboratorId) =>
      ref.watch(workSessionRepositoryProvider).history(collaboratorId),
);

/// Aggregated work-hours summary of a collaborator, keyed by collaborator id.
final workSummaryProvider = FutureProvider.family<WorkSummary, String>(
  (ref, collaboratorId) =>
      ref.watch(workSessionRepositoryProvider).summary(collaboratorId),
);
