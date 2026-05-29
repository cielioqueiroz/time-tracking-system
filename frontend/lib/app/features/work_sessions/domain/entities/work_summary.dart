class WorkSummary {
  const WorkSummary({
    required this.totalSessions,
    required this.finishedSessions,
    required this.totalMinutes,
  });

  final int totalSessions;
  final int finishedSessions;
  final int totalMinutes;

  bool get isEmpty => totalSessions == 0;
}
