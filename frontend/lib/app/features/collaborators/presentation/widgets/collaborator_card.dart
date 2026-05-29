import 'package:flutter/material.dart';

import '../../../../design_system/atoms/app_avatar.dart';
import '../../../../design_system/atoms/app_button.dart';
import '../../../../design_system/atoms/app_card.dart';
import '../../../../design_system/atoms/status_badge.dart';
import '../../../../design_system/foundations/app_theme_extension.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../../../design_system/tokens/app_typography.dart';
import '../../domain/entities/collaborator.dart';

/// Rich collaborator row: identity + live status + journey/edit/delete actions.
class CollaboratorCard extends StatelessWidget {
  const CollaboratorCard({
    super.key,
    required this.collaborator,
    required this.onStart,
    required this.onFinish,
    required this.onEdit,
    required this.onDelete,
    required this.onHistory,
    this.busy = false,
  });

  final Collaborator collaborator;
  final VoidCallback onStart;
  final VoidCallback onFinish;
  final VoidCallback onEdit;
  final VoidCallback onDelete;
  final VoidCallback onHistory;
  final bool busy;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    final working = collaborator.status.isWorking;

    return AppCard(
      padding: const EdgeInsets.all(AppSpacing.lg),
      child: Column(
        children: [
          Row(
            children: [
              AppAvatar(name: collaborator.name, size: 44),
              const SizedBox(width: AppSpacing.lg),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(collaborator.name,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: AppTypography.titleMd
                            .copyWith(color: palette.textPrimary, fontWeight: FontWeight.w700)),
                    const SizedBox(height: 2),
                    Text(collaborator.cargo,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: AppTypography.bodySm.copyWith(
                            color: palette.textSecondary, fontWeight: FontWeight.w600)),
                    Text(collaborator.email,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: AppTypography.caption.copyWith(color: palette.textMuted)),
                  ],
                ),
              ),
              const SizedBox(width: AppSpacing.md),
              StatusBadge(
                label: working ? 'Trabalhando' : 'Fora da jornada',
                color: working ? palette.success : palette.textMuted,
                pulse: working,
              ),
            ],
          ),
          const SizedBox(height: AppSpacing.lg),
          Divider(height: 1, color: palette.border),
          const SizedBox(height: AppSpacing.md),
          Row(
            children: [
              working
                  ? AppButton(
                      label: 'Encerrar jornada',
                      icon: Icons.stop_circle_outlined,
                      size: AppButtonSize.sm,
                      variant: AppButtonVariant.secondary,
                      loading: busy,
                      onPressed: onFinish,
                    )
                  : AppButton(
                      label: 'Iniciar jornada',
                      icon: Icons.play_circle_outline_rounded,
                      size: AppButtonSize.sm,
                      loading: busy,
                      onPressed: onStart,
                    ),
              const SizedBox(width: AppSpacing.sm),
              AppButton(
                label: 'Histórico',
                icon: Icons.history_rounded,
                size: AppButtonSize.sm,
                variant: AppButtonVariant.ghost,
                onPressed: onHistory,
              ),
              const Spacer(),
              _OverflowMenu(onEdit: onEdit, onDelete: onDelete),
            ],
          ),
        ],
      ),
    );
  }
}

class _OverflowMenu extends StatelessWidget {
  const _OverflowMenu({required this.onEdit, required this.onDelete});

  final VoidCallback onEdit;
  final VoidCallback onDelete;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return PopupMenuButton<int>(
      tooltip: 'Mais ações',
      color: palette.surface,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
        side: BorderSide(color: palette.border),
      ),
      icon: Icon(Icons.more_horiz_rounded, color: palette.textSecondary),
      onSelected: (v) => v == 0 ? onEdit() : onDelete(),
      itemBuilder: (context) => [
        PopupMenuItem(
          value: 0,
          child: Row(children: [
            Icon(Icons.edit_outlined, size: 18, color: palette.textSecondary),
            const SizedBox(width: 10),
            Text('Editar', style: AppTypography.bodyMd.copyWith(color: palette.textPrimary)),
          ]),
        ),
        PopupMenuItem(
          value: 1,
          child: Row(children: [
            Icon(Icons.delete_outline_rounded, size: 18, color: palette.danger),
            const SizedBox(width: 10),
            Text('Excluir', style: AppTypography.bodyMd.copyWith(color: palette.danger)),
          ]),
        ),
      ],
    );
  }
}
