/// Presentation formatting helpers — pure functions, no widget concerns.
abstract final class Formatters {
  static const _months = [
    'jan', 'fev', 'mar', 'abr', 'mai', 'jun',
    'jul', 'ago', 'set', 'out', 'nov', 'dez',
  ];

  /// Worked minutes → "8h 30m" / "45m" / "0m".
  static String duration(int? minutes) {
    if (minutes == null) return '—';
    final h = minutes ~/ 60;
    final m = minutes % 60;
    if (h == 0) return '${m}m';
    if (m == 0) return '${h}h';
    return '${h}h ${m}m';
  }

  /// UTC instant → local "28 mai 2026".
  static String date(DateTime dateTime) {
    final d = dateTime.toLocal();
    return '${d.day.toString().padLeft(2, '0')} ${_months[d.month - 1]} ${d.year}';
  }

  /// UTC instant → local "14:30".
  static String time(DateTime dateTime) {
    final d = dateTime.toLocal();
    return '${d.hour.toString().padLeft(2, '0')}:${d.minute.toString().padLeft(2, '0')}';
  }
}
