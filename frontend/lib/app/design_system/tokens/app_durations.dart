import 'package:flutter/animation.dart';

/// Motion tokens — durations and easing curves for microinteractions.
///
/// Keep animations short and subtle (premium feel, never flashy).
abstract final class AppDurations {
  static const Duration instant = Duration(milliseconds: 80);
  static const Duration fast = Duration(milliseconds: 150);
  static const Duration normal = Duration(milliseconds: 240);
  static const Duration slow = Duration(milliseconds: 360);

  /// Standard easing for most UI transitions.
  static const Curve easeStandard = Curves.easeInOutCubic;

  /// Emphasized easing for elements entering the screen.
  static const Curve easeEmphasized = Curves.easeOutCubic;
}
