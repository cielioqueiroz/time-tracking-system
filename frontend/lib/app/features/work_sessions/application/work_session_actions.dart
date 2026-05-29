import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../collaborators/application/collaborators_controller.dart';
import '../domain/repositories/work_session_repository.dart';
import 'work_session_providers.dart';

class WorkSessionActions {
  WorkSessionActions(this._ref);

  final Ref _ref;

  WorkSessionRepository get _repo => _ref.read(workSessionRepositoryProvider);

  Future<void> start(String collaboratorId) async {
    await _repo.start(collaboratorId);
    _afterChange(collaboratorId);
  }

  Future<void> finish(String collaboratorId) async {
    await _repo.finish(collaboratorId);
    _afterChange(collaboratorId);
  }

  void _afterChange(String collaboratorId) {
    _ref.invalidate(collaboratorsControllerProvider);
    _ref.invalidate(workSessionHistoryProvider(collaboratorId));
    _ref.invalidate(workSummaryProvider(collaboratorId));
  }
}

final workSessionActionsProvider =
    Provider<WorkSessionActions>(WorkSessionActions.new);
