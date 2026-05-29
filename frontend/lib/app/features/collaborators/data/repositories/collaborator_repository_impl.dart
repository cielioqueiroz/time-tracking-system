import '../../../../core/network/error_mapper.dart';
import '../../domain/entities/collaborator.dart';
import '../../domain/repositories/collaborator_repository.dart';
import '../datasources/collaborator_remote_datasource.dart';

class CollaboratorRepositoryImpl implements CollaboratorRepository {
  CollaboratorRepositoryImpl(this._remote);

  final CollaboratorRemoteDataSource _remote;

  @override
  Future<List<Collaborator>> list({int page = 0, int size = 50}) =>
      _guard(() => _remote.list(page: page, size: size));

  @override
  Future<Collaborator> create({
    required String name,
    required String email,
    required String cargo,
  }) =>
      _guard(() => _remote.create(name: name, email: email, cargo: cargo));

  @override
  Future<Collaborator> update({
    required String id,
    required String name,
    required String email,
    required String cargo,
  }) =>
      _guard(() => _remote.update(id: id, name: name, email: email, cargo: cargo));

  @override
  Future<void> delete(String id) => _guard(() => _remote.delete(id));

  Future<T> _guard<T>(Future<T> Function() action) async {
    try {
      return await action();
    } catch (e) {
      throw mapError(e);
    }
  }
}
