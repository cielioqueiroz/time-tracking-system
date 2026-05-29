import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/errors/failure.dart';
import '../../../../design_system/molecules/app_skeleton.dart';
import '../../../../design_system/molecules/state_views.dart';
import '../../../../design_system/layouts/app_page.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../../../shared/widgets/fade_slide_in.dart';
import '../../../collaborators/application/collaborators_controller.dart';
import '../../application/work_session_providers.dart';
import '../widgets/export_csv_button.dart';
import '../widgets/timeline_tile.dart';
import '../widgets/work_summary_card.dart';

/// Elegant timeline of a collaborator's work sessions.
class WorkSessionHistoryScreen extends ConsumerWidget {
  const WorkSessionHistoryScreen({super.key, required this.collaboratorId});

  final String collaboratorId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final history = ref.watch(workSessionHistoryProvider(collaboratorId));
    final name = ref
        .watch(collaboratorsControllerProvider)
        .valueOrNull
        ?.where((c) => c.id == collaboratorId)
        .firstOrNull
        ?.name;

    return AppPage(
      title: 'Histórico de jornadas',
      subtitle: name != null ? 'Jornadas de $name' : 'Linha do tempo de jornadas',
      onBack: () => context.pop(),
      maxContentWidth: 720,
      actions: [ExportCsvButton(collaboratorId: collaboratorId)],
      body: Column(
        children: [
          WorkSummaryCard(collaboratorId: collaboratorId),
          Expanded(
            child: history.when(
              loading: () => const _LoadingTimeline(),
              error: (error, _) => ErrorStateView(
                message: error is Failure
                    ? error.message
                    : 'Não foi possível carregar o histórico.',
                onRetry: () => _refresh(ref),
              ),
              data: (sessions) {
                if (sessions.isEmpty) {
                  return const EmptyStateView(
                    icon: Icons.timeline_rounded,
                    title: 'Sem jornadas registradas',
                    message:
                        'Quando este colaborador iniciar uma jornada, ela aparecerá aqui.',
                  );
                }
                return RefreshIndicator(
                  onRefresh: () async => _refresh(ref),
                  child: ListView.builder(
                    padding: const EdgeInsets.only(
                        top: AppSpacing.sm, bottom: AppSpacing.huge),
                    itemCount: sessions.length,
                    itemBuilder: (context, index) {
                      return FadeSlideIn(
                        delay: Duration(milliseconds: 40 * index),
                        child: TimelineTile(
                          session: sessions[index],
                          isFirst: index == 0,
                          isLast: index == sessions.length - 1,
                        ),
                      );
                    },
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  void _refresh(WidgetRef ref) {
    ref.invalidate(workSessionHistoryProvider(collaboratorId));
    ref.invalidate(workSummaryProvider(collaboratorId));
  }
}

class _LoadingTimeline extends StatelessWidget {
  const _LoadingTimeline();

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      itemCount: 4,
      separatorBuilder: (_, __) => const SizedBox(height: AppSpacing.lg),
      itemBuilder: (context, __) => Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: const [
          AppSkeleton(width: 12, height: 12, radius: BorderRadius.all(Radius.circular(6))),
          SizedBox(width: AppSpacing.lg),
          Expanded(child: AppSkeleton(height: 88, radius: BorderRadius.all(Radius.circular(16)))),
        ],
      ),
    );
  }
}
