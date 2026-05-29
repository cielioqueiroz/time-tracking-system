import 'package:flutter/animation.dart';

abstract final class AppDurations {
  static const Duration instant = Duration(milliseconds: 80);
  static const Duration fast = Duration(milliseconds: 150);
  static const Duration normal = Duration(milliseconds: 240);
  static const Duration slow = Duration(milliseconds: 360);

  static const Curve easeStandard = Curves.easeInOutCubic;

  static const Curve easeEmphasized = Curves.easeOutCubic;
}
