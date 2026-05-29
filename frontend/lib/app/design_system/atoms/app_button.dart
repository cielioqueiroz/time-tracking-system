import 'package:flutter/material.dart';

import '../foundations/app_theme_extension.dart';
import '../tokens/app_colors.dart';
import '../tokens/app_durations.dart';
import '../tokens/app_radius.dart';
import '../tokens/app_typography.dart';

enum AppButtonVariant { primary, secondary, ghost, danger }

enum AppButtonSize { sm, md }

/// The single button used across the product. Variants + loading state, with a
/// gentle hover/press feedback. No business logic.
class AppButton extends StatefulWidget {
  const AppButton({
    super.key,
    required this.label,
    this.onPressed,
    this.variant = AppButtonVariant.primary,
    this.size = AppButtonSize.md,
    this.icon,
    this.loading = false,
    this.expand = false,
  });

  final String label;
  final VoidCallback? onPressed;
  final AppButtonVariant variant;
  final AppButtonSize size;
  final IconData? icon;
  final bool loading;
  final bool expand;

  @override
  State<AppButton> createState() => _AppButtonState();
}

class _AppButtonState extends State<AppButton> {
  bool _hovered = false;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    final disabled = widget.onPressed == null || widget.loading;
    final colors = _resolveColors(palette);

    final height = widget.size == AppButtonSize.sm ? 36.0 : 44.0;
    final hPad = widget.size == AppButtonSize.sm ? 14.0 : 18.0;

    return Opacity(
      opacity: disabled && !widget.loading ? 0.55 : 1,
      child: MouseRegion(
        cursor: disabled ? SystemMouseCursors.basic : SystemMouseCursors.click,
        onEnter: (_) => setState(() => _hovered = true),
        onExit: (_) => setState(() => _hovered = false),
        child: GestureDetector(
          onTap: disabled ? null : widget.onPressed,
          child: AnimatedContainer(
            duration: AppDurations.fast,
            height: height,
            width: widget.expand ? double.infinity : null,
            padding: EdgeInsets.symmetric(horizontal: hPad),
            decoration: BoxDecoration(
              color: _hovered && !disabled ? colors.hoverBg : colors.bg,
              borderRadius: AppRadius.brMd,
              border: colors.border != null
                  ? Border.all(color: colors.border!)
                  : null,
            ),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                if (widget.loading)
                  SizedBox(
                    width: 16,
                    height: 16,
                    child: CircularProgressIndicator(
                        strokeWidth: 2, color: colors.fg),
                  )
                else if (widget.icon != null) ...[
                  Icon(widget.icon, size: 18, color: colors.fg),
                  const SizedBox(width: 8),
                ],
                if (!widget.loading)
                  Text(
                    widget.label,
                    style: AppTypography.label.copyWith(
                        color: colors.fg, fontWeight: FontWeight.w600),
                  ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  _BtnColors _resolveColors(AppPalette p) {
    return switch (widget.variant) {
      AppButtonVariant.primary => _BtnColors(
          bg: p.accent, hoverBg: p.accentHover, fg: p.onAccent),
      AppButtonVariant.secondary => _BtnColors(
          bg: p.surface, hoverBg: p.surfaceMuted, fg: p.textPrimary, border: p.border),
      AppButtonVariant.ghost => _BtnColors(
          bg: Colors.transparent, hoverBg: p.surfaceMuted, fg: p.textSecondary),
      AppButtonVariant.danger => _BtnColors(
          bg: p.danger, hoverBg: p.danger.withValues(alpha: 0.85), fg: Colors.white),
    };
  }
}

class _BtnColors {
  _BtnColors({required this.bg, required this.hoverBg, required this.fg, this.border});
  final Color bg;
  final Color hoverBg;
  final Color fg;
  final Color? border;
}
