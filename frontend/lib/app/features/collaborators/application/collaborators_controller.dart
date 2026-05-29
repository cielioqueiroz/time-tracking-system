import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../domain/entities/collaborator.dart';
import '../domain/repositories/collaborator_repository.dart';
import 'collaborator_providers.dart';

/// State controller for the collaborator list.
///
/// Holds the async list state; mutations delegate to the repository then
/// refresh. Errors are rethrown as `Failure` so the UI can present feedback
/// while the list state stays intact.
class CollaboratorsController extends AsyncNotifier<List<Collaborator>> {
  CollaboratorRepository get _repo => ref.read(collaboratorRepositoryProvider);

  @override
  Future<List<Collaborator>> build() => _repo.list();

  Future<void> refresh() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(_repo.list);
  }

  Future<void> create({required String name, required String email}) async {
    await _repo.create(name: name, email: email);
    await refresh();
  }

  Future<void> edit({
    required String id,
    required String name,
    required String email,
  }) async {
    await _repo.update(id: id, name: name, email: email);
    await refresh();
  }

  Future<void> delete(String id) async {
    await _repo.delete(id);
    await refresh();
  }
}

final collaboratorsControllerProvider =
    AsyncNotifierProvider<CollaboratorsController, List<Collaborator>>(
        CollaboratorsController.new);
