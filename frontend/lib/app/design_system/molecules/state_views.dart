import 'package:flutter/material.dart';

import '../atoms/app_button.dart';
import '../foundations/app_theme_extension.dart';
import '../tokens/app_radius.dart';
import '../tokens/app_spacing.dart';
import '../tokens/app_typography.dart';

class EmptyStateView extends StatelessWidget {
  const EmptyStateView({
    super.key,
    required this.icon,
    required this.title,
    required this.message,
    this.actionLabel,
    this.onAction,
  });

  final IconData icon;
  final String title;
  final String message;
  final String? actionLabel;
  final VoidCallback? onAction;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(AppSpacing.xxl),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 64,
              height: 64,
              decoration: BoxDecoration(
                color: palette.surfaceMuted,
                borderRadius: AppRadius.brLg,
                border: Border.all(color: palette.border),
              ),
              child: Icon(icon, color: palette.textMuted, size: 28),
            ),
            const SizedBox(height: AppSpacing.lg),
            Text(title, style: AppTypography.headingSm.copyWith(color: palette.textPrimary)),
            const SizedBox(height: AppSpacing.xs),
            SizedBox(
              width: 320,
              child: Text(
                message,
                textAlign: TextAlign.center,
                style: AppTypography.bodyMd.copyWith(color: palette.textSecondary),
              ),
            ),
            if (actionLabel != null && onAction != null) ...[
              const SizedBox(height: AppSpacing.xl),
              AppButton(label: actionLabel!, icon: Icons.add_rounded, onPressed: onAction),
            ],
          ],
        ),
      ),
    );
  }
}

class ErrorStateView extends StatelessWidget {
  const ErrorStateView({super.key, required this.message, this.onRetry});

  final String message;
  final VoidCallback? onRetry;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(AppSpacing.xxl),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 64,
              height: 64,
              decoration: BoxDecoration(
                color: palette.danger.withValues(alpha: 0.1),
                borderRadius: AppRadius.brLg,
                border: Border.all(color: palette.danger.withValues(alpha: 0.25)),
              ),
              child: Icon(Icons.error_outline_rounded, color: palette.danger, size: 28),
            ),
            const SizedBox(height: AppSpacing.lg),
            Text('Algo deu errado',
                style: AppTypography.headingSm.copyWith(color: palette.textPrimary)),
            const SizedBox(height: AppSpacing.xs),
            SizedBox(
              width: 320,
              child: Text(message,
                  textAlign: TextAlign.center,
                  style: AppTypography.bodyMd.copyWith(color: palette.textSecondary)),
            ),
            if (onRetry != null) ...[
              const SizedBox(height: AppSpacing.xl),
              AppButton(
                label: 'Tentar novamente',
                icon: Icons.refresh_rounded,
                variant: AppButtonVariant.secondary,
                onPressed: onRetry,
              ),
            ],
          ],
        ),
      ),
    );
  }
}
