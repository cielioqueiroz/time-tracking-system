import '../../domain/entities/collaborator.dart';

abstract final class CollaboratorModel {
  static Collaborator fromJson(Map<String, dynamic> json) {
    return Collaborator(
      id: json['id'] as String,
      name: json['name'] as String,
      email: json['email'] as String,
      cargo: json['cargo'] as String,
      status: CollaboratorStatus.fromApi(json['status'] as String),
    );
  }

  static Map<String, dynamic> toWriteJson({
    required String name,
    required String email,
    required String cargo,
  }) {
    return {'name': name, 'email': email, 'cargo': cargo};
  }
}
