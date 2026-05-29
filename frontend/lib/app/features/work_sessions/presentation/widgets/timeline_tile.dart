import 'package:flutter/material.dart';

import '../../../../core/utils/formatters.dart';
import '../../../../design_system/atoms/app_card.dart';
import '../../../../design_system/atoms/status_badge.dart';
import '../../../../design_system/foundations/app_fonts.dart';
import '../../../../design_system/foundations/app_theme_extension.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../../../design_system/tokens/app_typography.dart';
import '../../domain/entities/work_session.dart';

class TimelineTile extends StatelessWidget {
  const TimelineTile({
    super.key,
    required this.session,
    required this.isFirst,
    required this.isLast,
  });

  final WorkSession session;
  final bool isFirst;
  final bool isLast;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    final open = session.status.isOpen;
    final accent = open ? palette.success : palette.accent;

    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          _Rail(color: palette.border, dotColor: accent, isFirst: isFirst, isLast: isLast),
          const SizedBox(width: AppSpacing.lg),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(bottom: AppSpacing.lg),
              child: AppCard(
                padding: const EdgeInsets.all(AppSpacing.lg),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Text(Formatters.date(session.startedAt),
                            style: AppTypography.titleMd.copyWith(
                                color: palette.textPrimary, fontWeight: FontWeight.w700)),
                        const Spacer(),
                        StatusBadge(
                          label: open ? 'Em andamento' : 'Finalizada',
                          color: accent,
                          pulse: open,
                        ),
                      ],
                    ),
                    const SizedBox(height: AppSpacing.md),
                    Row(
                      children: [
                        _TimeBlock(label: 'Entrada', value: Formatters.time(session.startedAt)),
                        const SizedBox(width: AppSpacing.xl),
                        _TimeBlock(
                          label: 'Saída',
                          value: session.endedAt == null ? '—' : Formatters.time(session.endedAt!),
                        ),
                        const Spacer(),
                        _DurationChip(minutes: session.totalMinutes, open: open),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _Rail extends StatelessWidget {
  const _Rail({
    required this.color,
    required this.dotColor,
    required this.isFirst,
    required this.isLast,
  });

  final Color color;
  final Color dotColor;
  final bool isFirst;
  final bool isLast;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return SizedBox(
      width: 16,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(height: 6, child: Center(
            child: Container(width: 2, color: isFirst ? Colors.transparent : color))),
          Container(
            width: 12,
            height: 12,
            decoration: BoxDecoration(
              color: palette.surface,
              shape: BoxShape.circle,
              border: Border.all(color: dotColor, width: 3),
            ),
          ),
          Expanded(child: Center(
            child: Container(width: 2, color: isLast ? Colors.transparent : color))),
        ],
      ),
    );
  }
}

class _TimeBlock extends StatelessWidget {
  const _TimeBlock({required this.label, required this.value});
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(label, style: AppTypography.caption.copyWith(color: palette.textMuted)),
        const SizedBox(height: 2),
        Text(value,
            style: AppFonts.mono(AppTypography.bodyMd.copyWith(
                color: palette.textPrimary, fontWeight: FontWeight.w600))),
      ],
    );
  }
}

class _DurationChip extends StatelessWidget {
  const _DurationChip({required this.minutes, required this.open});
  final int? minutes;
  final bool open;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        color: palette.surfaceMuted,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: palette.border),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(Icons.schedule_rounded, size: 15, color: palette.textMuted),
          const SizedBox(width: 6),
          Text(
            open ? 'em curso' : Formatters.duration(minutes),
            style: AppFonts.mono(AppTypography.bodySm.copyWith(
                color: palette.textSecondary, fontWeight: FontWeight.w600)),
          ),
        ],
      ),
    );
  }
}
