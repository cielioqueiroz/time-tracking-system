import 'package:flutter/material.dart';

/// Typographic scale — sizes, weights and line-heights only.
///
/// Colors are applied by the theme, not here, so the same scale serves both
/// light and dark modes.
abstract final class AppTypography {
  static const String fontFamily = 'Inter';

  static const TextStyle displayLg = TextStyle(
    fontSize: 34, height: 1.15, fontWeight: FontWeight.w700, letterSpacing: -0.6);
  static const TextStyle displaySm = TextStyle(
    fontSize: 28, height: 1.2, fontWeight: FontWeight.w700, letterSpacing: -0.4);
  static const TextStyle headingLg = TextStyle(
    fontSize: 22, height: 1.25, fontWeight: FontWeight.w600, letterSpacing: -0.2);
  static const TextStyle headingSm = TextStyle(
    fontSize: 18, height: 1.3, fontWeight: FontWeight.w600);
  static const TextStyle titleMd = TextStyle(
    fontSize: 16, height: 1.4, fontWeight: FontWeight.w600);
  static const TextStyle bodyLg = TextStyle(
    fontSize: 16, height: 1.5, fontWeight: FontWeight.w400);
  static const TextStyle bodyMd = TextStyle(
    fontSize: 14, height: 1.5, fontWeight: FontWeight.w400);
  static const TextStyle bodySm = TextStyle(
    fontSize: 13, height: 1.45, fontWeight: FontWeight.w400);
  static const TextStyle label = TextStyle(
    fontSize: 13, height: 1.3, fontWeight: FontWeight.w500, letterSpacing: 0.1);
  static const TextStyle caption = TextStyle(
    fontSize: 12, height: 1.3, fontWeight: FontWeight.w500, letterSpacing: 0.2);
}
