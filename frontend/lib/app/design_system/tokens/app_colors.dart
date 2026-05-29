import 'package:flutter/material.dart';

@immutable
class AppPalette {
  const AppPalette({
    required this.background,
    required this.surface,
    required this.surfaceMuted,
    required this.border,
    required this.textPrimary,
    required this.textSecondary,
    required this.textMuted,
    required this.accent,
    required this.accentHover,
    required this.onAccent,
    required this.success,
    required this.warning,
    required this.danger,
    required this.info,
    required this.brightness,
  });

  final Color background;
  final Color surface;
  final Color surfaceMuted;
  final Color border;
  final Color textPrimary;
  final Color textSecondary;
  final Color textMuted;
  final Color accent;
  final Color accentHover;
  final Color onAccent;
  final Color success;
  final Color warning;
  final Color danger;
  final Color info;
  final Brightness brightness;

  static const AppPalette light = AppPalette(
    background: Color(0xFFF8F9FB),
    surface: Color(0xFFFFFFFF),
    surfaceMuted: Color(0xFFF1F3F6),
    border: Color(0xFFE3E6EB),
    textPrimary: Color(0xFF11151C),
    textSecondary: Color(0xFF4A5260),
    textMuted: Color(0xFF8A93A2),
    accent: Color(0xFF5B5BD6),
    accentHover: Color(0xFF4A4AC4),
    onAccent: Color(0xFFFFFFFF),
    success: Color(0xFF1A9D5A),
    warning: Color(0xFFD98A0B),
    danger: Color(0xFFD92D20),
    info: Color(0xFF1570EF),
    brightness: Brightness.light,
  );

  static const AppPalette dark = AppPalette(
    background: Color(0xFF0B0D11),
    surface: Color(0xFF14171D),
    surfaceMuted: Color(0xFF1C2027),
    border: Color(0xFF272C35),
    textPrimary: Color(0xFFF2F4F8),
    textSecondary: Color(0xFFAEB6C3),
    textMuted: Color(0xFF6B7382),
    accent: Color(0xFF7C7CF0),
    accentHover: Color(0xFF8E8EF5),
    onAccent: Color(0xFF0B0D11),
    success: Color(0xFF3DCB7F),
    warning: Color(0xFFF0B33A),
    danger: Color(0xFFF26B62),
    info: Color(0xFF4D95F5),
    brightness: Brightness.dark,
  );
}
