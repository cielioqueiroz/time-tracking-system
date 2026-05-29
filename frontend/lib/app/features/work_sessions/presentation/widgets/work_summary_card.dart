import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/utils/formatters.dart';
import '../../../../design_system/atoms/app_card.dart';
import '../../../../design_system/foundations/app_fonts.dart';
import '../../../../design_system/foundations/app_theme_extension.dart';
import '../../../../design_system/molecules/app_skeleton.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../../../design_system/tokens/app_typography.dart';
import '../../application/work_session_providers.dart';

/// Header card with the collaborator's aggregated work-hours stats.
/// Has its own loading/error handling so it never blocks the timeline.
class WorkSummaryCard extends ConsumerWidget {
  const WorkSummaryCard({super.key, required this.collaboratorId});

  final String collaboratorId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final summary = ref.watch(workSummaryProvider(collaboratorId));

    return summary.when(
      loading: () => const Padding(
        padding: EdgeInsets.symmetric(vertical: AppSpacing.sm),
        child: AppSkeleton(height: 92, radius: BorderRadius.all(Radius.circular(16))),
      ),
      // Silent on error: the timeline below already surfaces failures.
      error: (_, __) => const SizedBox.shrink(),
      data: (s) {
        final palette = context.palette;
        return Padding(
          padding: const EdgeInsets.symmetric(vertical: AppSpacing.sm),
          child: AppCard(
            padding: const EdgeInsets.all(AppSpacing.lg),
            child: Row(
              children: [
                Container(
                  width: 44,
                  height: 44,
                  decoration: BoxDecoration(
                    color: palette.accent.withValues(alpha: 0.12),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Icon(Icons.timelapse_rounded, color: palette.accent, size: 24),
                ),
                const SizedBox(width: AppSpacing.lg),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Total trabalhado',
                          style: AppTypography.caption.copyWith(color: palette.textMuted)),
                      const SizedBox(height: 2),
                      Text(
                        Formatters.duration(s.totalMinutes),
                        style: AppFonts.mono(AppTypography.headingLg.copyWith(
                            color: palette.textPrimary, fontWeight: FontWeight.w700)),
                      ),
                    ],
                  ),
                ),
                _Stat(label: 'Jornadas', value: '${s.totalSessions}'),
                const SizedBox(width: AppSpacing.xl),
                _Stat(label: 'Finalizadas', value: '${s.finishedSessions}'),
              ],
            ),
          ),
        );
      },
    );
  }
}

class _Stat extends StatelessWidget {
  const _Stat({required this.label, required this.value});
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Text(value,
            style: AppFonts.mono(AppTypography.titleMd.copyWith(
                color: palette.textPrimary, fontWeight: FontWeight.w700))),
        const SizedBox(height: 2),
        Text(label, style: AppTypography.caption.copyWith(color: palette.textMuted)),
      ],
    );
  }
}
