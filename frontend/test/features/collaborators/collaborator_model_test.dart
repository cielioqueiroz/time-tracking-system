import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/features/collaborators/data/models/collaborator_model.dart';
import 'package:time_tracking/app/features/collaborators/domain/entities/collaborator.dart';

void main() {
  group('CollaboratorModel.fromJson', () {
    test('maps a working collaborator payload', () {
      final collaborator = CollaboratorModel.fromJson({
        'id': '11111111-1111-1111-1111-111111111111',
        'name': 'Ana Souza',
        'email': 'ana@empresa.com',
        'status': 'TRABALHANDO',
      });

      expect(collaborator.id, '11111111-1111-1111-1111-111111111111');
      expect(collaborator.name, 'Ana Souza');
      expect(collaborator.email, 'ana@empresa.com');
      expect(collaborator.status, CollaboratorStatus.trabalhando);
      expect(collaborator.status.isWorking, isTrue);
    });

    test('maps an off-duty collaborator payload', () {
      final collaborator = CollaboratorModel.fromJson({
        'id': 'x',
        'name': 'Bob',
        'email': 'bob@empresa.com',
        'status': 'FORA_DA_JORNADA',
      });

      expect(collaborator.status, CollaboratorStatus.foraDaJornada);
      expect(collaborator.status.isWorking, isFalse);
    });

    test('unknown status falls back to off-duty', () {
      final collaborator = CollaboratorModel.fromJson({
        'id': 'x',
        'name': 'Bob',
        'email': 'bob@empresa.com',
        'status': 'WHATEVER',
      });

      expect(collaborator.status, CollaboratorStatus.foraDaJornada);
    });
  });

  test('toWriteJson serializes only name and email', () {
    final json = CollaboratorModel.toWriteJson(name: 'Ana', email: 'ana@empresa.com');

    expect(json, {'name': 'Ana', 'email': 'ana@empresa.com'});
  });
}
