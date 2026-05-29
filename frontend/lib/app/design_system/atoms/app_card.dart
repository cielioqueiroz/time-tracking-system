import 'package:flutter/material.dart';

import '../foundations/app_theme_extension.dart';
import '../tokens/app_durations.dart';
import '../tokens/app_elevation.dart';
import '../tokens/app_radius.dart';

/// Surface container — the base building block for elevated content.
/// Optionally interactive, with a subtle hover lift on pointer devices.
class AppCard extends StatefulWidget {
  const AppCard({
    super.key,
    required this.child,
    this.padding = const EdgeInsets.all(16),
    this.onTap,
  });

  final Widget child;
  final EdgeInsetsGeometry padding;
  final VoidCallback? onTap;

  @override
  State<AppCard> createState() => _AppCardState();
}

class _AppCardState extends State<AppCard> {
  bool _hovered = false;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    final interactive = widget.onTap != null;

    return MouseRegion(
      cursor: interactive ? SystemMouseCursors.click : MouseCursor.defer,
      onEnter: interactive ? (_) => setState(() => _hovered = true) : null,
      onExit: interactive ? (_) => setState(() => _hovered = false) : null,
      child: GestureDetector(
        onTap: widget.onTap,
        child: AnimatedContainer(
          duration: AppDurations.fast,
          curve: AppDurations.easeStandard,
          padding: widget.padding,
          decoration: BoxDecoration(
            color: palette.surface,
            borderRadius: AppRadius.brLg,
            border: Border.all(
              color: _hovered ? palette.accent.withValues(alpha: 0.45) : palette.border,
            ),
            boxShadow: _hovered ? AppElevation.md : AppElevation.sm,
          ),
          child: widget.child,
        ),
      ),
    );
  }
}
