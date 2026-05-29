import '../../domain/entities/collaborator.dart';

/// Maps the collaborator JSON payload to/from the domain [Collaborator].
/// Isolated here so the domain entity stays free of serialization concerns.
abstract final class CollaboratorModel {
  static Collaborator fromJson(Map<String, dynamic> json) {
    return Collaborator(
      id: json['id'] as String,
      name: json['name'] as String,
      email: json['email'] as String,
      status: CollaboratorStatus.fromApi(json['status'] as String),
    );
  }

  static Map<String, dynamic> toWriteJson({
    required String name,
    required String email,
  }) {
    return {'name': name, 'email': email};
  }
}
