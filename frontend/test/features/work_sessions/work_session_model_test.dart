import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/features/work_sessions/data/models/work_session_model.dart';
import 'package:time_tracking/app/features/work_sessions/domain/entities/work_session.dart';

void main() {
  group('WorkSessionModel.fromJson', () {
    test('maps an open session (no end / no total)', () {
      final session = WorkSessionModel.fromJson({
        'id': 's1',
        'collaboratorId': 'c1',
        'status': 'EM_ANDAMENTO',
        'startedAt': '2026-05-29T08:00:00Z',
        'endedAt': null,
        'totalMinutes': null,
      });

      expect(session.id, 's1');
      expect(session.collaboratorId, 'c1');
      expect(session.status, WorkSessionStatus.emAndamento);
      expect(session.status.isOpen, isTrue);
      expect(session.startedAt, DateTime.parse('2026-05-29T08:00:00Z'));
      expect(session.endedAt, isNull);
      expect(session.totalMinutes, isNull);
    });

    test('maps a finished session with end and total minutes', () {
      final session = WorkSessionModel.fromJson({
        'id': 's2',
        'collaboratorId': 'c1',
        'status': 'FINALIZADA',
        'startedAt': '2026-05-29T08:00:00Z',
        'endedAt': '2026-05-29T10:00:00Z',
        'totalMinutes': 120,
      });

      expect(session.status, WorkSessionStatus.finalizada);
      expect(session.status.isOpen, isFalse);
      expect(session.endedAt, DateTime.parse('2026-05-29T10:00:00Z'));
      expect(session.totalMinutes, 120);
    });
  });
}
