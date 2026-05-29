import 'package:flutter/material.dart';

import '../tokens/app_colors.dart';
import '../tokens/app_radius.dart';
import '../tokens/app_typography.dart';
import 'app_fonts.dart';
import 'app_theme_extension.dart';

/// Builds Material [ThemeData] for both brightness modes from design tokens.
///
/// This is the ONLY place tokens are translated into a Flutter theme; widgets
/// never construct colors or text styles ad-hoc.
abstract final class AppTheme {
  static ThemeData light() => _build(AppPalette.light);
  static ThemeData dark() => _build(AppPalette.dark);

  static ThemeData _build(AppPalette p) {
    final colorScheme = ColorScheme(
      brightness: p.brightness,
      primary: p.accent,
      onPrimary: p.onAccent,
      secondary: p.accent,
      onSecondary: p.onAccent,
      surface: p.surface,
      onSurface: p.textPrimary,
      error: p.danger,
      onError: Colors.white,
    );

    return ThemeData(
      useMaterial3: true,
      brightness: p.brightness,
      colorScheme: colorScheme,
      scaffoldBackgroundColor: p.background,
      textTheme: AppFonts.sansTextTheme(_textTheme(p.textPrimary)),
      dividerColor: p.border,
      splashFactory: NoSplash.splashFactory,
      extensions: [AppThemeExt(palette: p)],
      tooltipTheme: TooltipThemeData(
        decoration: BoxDecoration(
          color: p.textPrimary.withValues(alpha: 0.92),
          borderRadius: AppRadius.brSm,
        ),
        textStyle: AppFonts.sans(AppTypography.caption.copyWith(color: p.surface)),
      ),
    );
  }

  static TextTheme _textTheme(Color color) => TextTheme(
        displayLarge: AppTypography.displayLg.copyWith(color: color),
        displaySmall: AppTypography.displaySm.copyWith(color: color),
        headlineMedium: AppTypography.headingLg.copyWith(color: color),
        headlineSmall: AppTypography.headingSm.copyWith(color: color),
        titleMedium: AppTypography.titleMd.copyWith(color: color),
        bodyLarge: AppTypography.bodyLg.copyWith(color: color),
        bodyMedium: AppTypography.bodyMd.copyWith(color: color),
        bodySmall: AppTypography.bodySm.copyWith(color: color),
        labelLarge: AppTypography.label.copyWith(color: color),
        labelSmall: AppTypography.caption.copyWith(color: color),
      );
}
