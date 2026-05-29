import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/features/work_sessions/data/models/work_summary_model.dart';

void main() {
  group('WorkSummaryModel.fromJson', () {
    test('maps aggregated fields', () {
      final summary = WorkSummaryModel.fromJson({
        'collaboratorId': 'c1',
        'totalSessions': 5,
        'finishedSessions': 4,
        'totalMinutes': 480,
      });

      expect(summary.totalSessions, 5);
      expect(summary.finishedSessions, 4);
      expect(summary.totalMinutes, 480);
      expect(summary.isEmpty, isFalse);
    });

    test('zeroed summary is empty', () {
      final summary = WorkSummaryModel.fromJson({
        'collaboratorId': 'c1',
        'totalSessions': 0,
        'finishedSessions': 0,
        'totalMinutes': 0,
      });

      expect(summary.isEmpty, isTrue);
    });
  });
}
