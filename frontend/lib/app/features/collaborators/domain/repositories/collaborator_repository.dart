import '../entities/collaborator.dart';

abstract interface class CollaboratorRepository {
  Future<List<Collaborator>> list({int page = 0, int size = 50});

  Future<Collaborator> create({
    required String name,
    required String email,
    required String cargo,
  });

  Future<Collaborator> update({
    required String id,
    required String name,
    required String email,
    required String cargo,
  });

  Future<void> delete(String id);
}
