import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../core/theme/theme_mode_controller.dart';
import '../../design_system/foundations/app_theme_extension.dart';

/// Compact icon button that toggles light/dark mode.
class ThemeToggleButton extends ConsumerWidget {
  const ThemeToggleButton({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final palette = context.palette;
    final isDark = Theme.of(context).brightness == Brightness.dark;

    return IconButton(
      tooltip: isDark ? 'Tema claro' : 'Tema escuro',
      onPressed: () => ref.read(themeModeProvider.notifier).toggle(),
      icon: Icon(
        isDark ? Icons.light_mode_rounded : Icons.dark_mode_rounded,
        color: palette.textSecondary,
        size: 20,
      ),
      style: IconButton.styleFrom(
        backgroundColor: palette.surfaceMuted,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
          side: BorderSide(color: palette.border),
        ),
      ),
    );
  }
}
