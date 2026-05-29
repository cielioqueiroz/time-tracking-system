import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/core/utils/formatters.dart';

void main() {
  group('Formatters.duration', () {
    test('null minutes renders an em dash', () {
      expect(Formatters.duration(null), '—');
    });

    test('only minutes', () {
      expect(Formatters.duration(45), '45m');
      expect(Formatters.duration(0), '0m');
    });

    test('whole hours drop the minutes part', () {
      expect(Formatters.duration(120), '2h');
    });

    test('hours and minutes', () {
      expect(Formatters.duration(510), '8h 30m');
    });
  });

  group('Formatters date/time', () {
    test('date renders day, abbreviated month and year (local)', () {
      // Build from a local DateTime to avoid timezone drift in the assertion.
      final d = DateTime(2026, 5, 28, 14, 30);
      expect(Formatters.date(d), '28 mai 2026');
    });

    test('time is zero-padded HH:mm (local)', () {
      final d = DateTime(2026, 1, 9, 9, 5);
      expect(Formatters.time(d), '09:05');
    });
  });
}
