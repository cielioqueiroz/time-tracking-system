import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/errors/failure.dart';
import '../../../../design_system/atoms/app_button.dart';
import '../../../../design_system/atoms/app_card.dart';
import '../../../../design_system/layouts/app_page.dart';
import '../../../../design_system/molecules/app_dialog.dart';
import '../../../../design_system/molecules/app_feedback.dart';
import '../../../../design_system/molecules/app_skeleton.dart';
import '../../../../design_system/molecules/state_views.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../../../routes/app_routes.dart';
import '../../../../shared/widgets/fade_slide_in.dart';
import '../../../../shared/widgets/theme_toggle_button.dart';
import '../../../work_sessions/application/work_session_actions.dart';
import '../../application/collaborators_controller.dart';
import '../../domain/entities/collaborator.dart';
import '../widgets/collaborator_card.dart';

/// Main screen: lists collaborators with live status and journey actions.
class CollaboratorsScreen extends ConsumerStatefulWidget {
  const CollaboratorsScreen({super.key});

  @override
  ConsumerState<CollaboratorsScreen> createState() => _CollaboratorsScreenState();
}

class _CollaboratorsScreenState extends ConsumerState<CollaboratorsScreen> {
  String? _busyId;

  Future<void> _runJourneyAction(
    Collaborator c,
    Future<void> Function(String) action,
    String successMessage,
  ) async {
    setState(() => _busyId = c.id);
    try {
      await action(c.id);
      if (!mounted) return;
      AppFeedback.success(context, successMessage);
    } on Failure catch (f) {
      if (!mounted) return;
      AppFeedback.error(context, f.message);
    } finally {
      if (mounted) setState(() => _busyId = null);
    }
  }

  Future<void> _confirmDelete(Collaborator c) async {
    final confirmed = await showConfirmDialog(
      context,
      title: 'Excluir colaborador',
      message: 'Tem certeza que deseja excluir "${c.name}"? '
          'Esta ação também remove o histórico de jornadas.',
      confirmLabel: 'Excluir',
      danger: true,
    );
    if (!confirmed) return;
    try {
      await ref.read(collaboratorsControllerProvider.notifier).delete(c.id);
      if (!mounted) return;
      AppFeedback.success(context, 'Colaborador excluído.');
    } on Failure catch (f) {
      if (!mounted) return;
      AppFeedback.error(context, f.message);
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(collaboratorsControllerProvider);

    return AppPage(
      title: 'Colaboradores',
      subtitle: state.maybeWhen(
        data: (list) => '${list.length} ${list.length == 1 ? 'colaborador' : 'colaboradores'}',
        orElse: () => 'Gestão de ponto da equipe',
      ),
      actions: [
        const ThemeToggleButton(),
        const SizedBox(width: AppSpacing.sm),
        AppButton(
          label: 'Novo colaborador',
          icon: Icons.add_rounded,
          onPressed: () => context.pushNamed(AppRoutes.newCollaboratorName),
        ),
      ],
      body: state.when(
        loading: () => const _LoadingList(),
        error: (error, _) => ErrorStateView(
          message: error is Failure ? error.message : 'Não foi possível carregar.',
          onRetry: () => ref.read(collaboratorsControllerProvider.notifier).refresh(),
        ),
        data: (collaborators) {
          if (collaborators.isEmpty) {
            return EmptyStateView(
              icon: Icons.groups_outlined,
              title: 'Nenhum colaborador ainda',
              message: 'Cadastre o primeiro colaborador para começar a registrar jornadas.',
              actionLabel: 'Novo colaborador',
              onAction: () => context.pushNamed(AppRoutes.newCollaboratorName),
            );
          }
          return RefreshIndicator(
            onRefresh: () => ref.read(collaboratorsControllerProvider.notifier).refresh(),
            child: ListView.separated(
              padding: const EdgeInsets.only(bottom: AppSpacing.huge),
              itemCount: collaborators.length,
              separatorBuilder: (_, __) => const SizedBox(height: AppSpacing.md),
              itemBuilder: (context, index) {
                final c = collaborators[index];
                return FadeSlideIn(
                  delay: Duration(milliseconds: 40 * index),
                  child: CollaboratorCard(
                    collaborator: c,
                    busy: _busyId == c.id,
                    onStart: () => _runJourneyAction(
                        c, ref.read(workSessionActionsProvider).start, 'Jornada iniciada.'),
                    onFinish: () => _runJourneyAction(
                        c, ref.read(workSessionActionsProvider).finish, 'Jornada encerrada.'),
                    onEdit: () => context.pushNamed(
                      AppRoutes.editCollaboratorName,
                      pathParameters: {'id': c.id},
                    ),
                    onHistory: () => context.pushNamed(
                      AppRoutes.historyName,
                      pathParameters: {'id': c.id},
                    ),
                    onDelete: () => _confirmDelete(c),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}

/// Loading skeleton mimicking the collaborator cards.
class _LoadingList extends StatelessWidget {
  const _LoadingList();

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      itemCount: 5,
      separatorBuilder: (_, __) => const SizedBox(height: AppSpacing.md),
      itemBuilder: (context, __) => AppCard(
        padding: const EdgeInsets.all(AppSpacing.lg),
        child: Column(
          children: const [
            Row(
              children: [
                AppSkeleton(width: 44, height: 44, radius: BorderRadius.all(Radius.circular(22))),
                SizedBox(width: AppSpacing.lg),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      AppSkeleton(width: 160, height: 14),
                      SizedBox(height: 8),
                      AppSkeleton(width: 220, height: 12),
                    ],
                  ),
                ),
                SizedBox(width: AppSpacing.md),
                AppSkeleton(width: 110, height: 24, radius: BorderRadius.all(Radius.circular(999))),
              ],
            ),
            SizedBox(height: AppSpacing.lg),
            AppSkeleton(height: 36),
          ],
        ),
      ),
    );
  }
}
