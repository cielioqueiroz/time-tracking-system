import 'package:flutter/material.dart';

import '../tokens/app_colors.dart';

/// Exposes the design-system [AppPalette] through Flutter's theme so any widget
/// can read semantic colors via `Theme.of(context).extension<AppThemeExt>()`.
///
/// Prefer the [BuildContextPalette.palette] extension for ergonomics.
@immutable
class AppThemeExt extends ThemeExtension<AppThemeExt> {
  const AppThemeExt({required this.palette});

  final AppPalette palette;

  @override
  AppThemeExt copyWith({AppPalette? palette}) =>
      AppThemeExt(palette: palette ?? this.palette);

  @override
  AppThemeExt lerp(ThemeExtension<AppThemeExt>? other, double t) {
    // Palettes are discrete (light/dark); snap rather than interpolate.
    return this;
  }
}

/// Ergonomic access: `context.palette.accent`.
extension BuildContextPalette on BuildContext {
  AppPalette get palette => Theme.of(this).extension<AppThemeExt>()!.palette;
}
