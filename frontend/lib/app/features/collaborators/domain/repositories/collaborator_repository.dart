import '../entities/collaborator.dart';

/// Domain contract for collaborator operations. The presentation layer depends
/// on this abstraction, never on the HTTP implementation.
abstract interface class CollaboratorRepository {
  Future<List<Collaborator>> list({int page = 0, int size = 50});

  Future<Collaborator> create({required String name, required String email});

  Future<Collaborator> update({
    required String id,
    required String name,
    required String email,
  });

  Future<void> delete(String id);
}
