import 'package:flutter/material.dart';

import '../tokens/app_colors.dart';

@immutable
class AppThemeExt extends ThemeExtension<AppThemeExt> {
  const AppThemeExt({required this.palette});

  final AppPalette palette;

  @override
  AppThemeExt copyWith({AppPalette? palette}) =>
      AppThemeExt(palette: palette ?? this.palette);

  @override
  AppThemeExt lerp(ThemeExtension<AppThemeExt>? other, double t) {
    return this;
  }
}

extension BuildContextPalette on BuildContext {
  AppPalette get palette => Theme.of(this).extension<AppThemeExt>()!.palette;
}
