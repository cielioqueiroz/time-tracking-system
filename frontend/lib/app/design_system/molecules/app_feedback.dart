import 'package:flutter/material.dart';

import '../foundations/app_theme_extension.dart';
import '../tokens/app_radius.dart';
import '../tokens/app_typography.dart';

enum _FeedbackKind { success, error }

/// Lightweight, themed snackbars for transient feedback.
abstract final class AppFeedback {
  static void success(BuildContext context, String message) =>
      _show(context, message, _FeedbackKind.success);

  static void error(BuildContext context, String message) =>
      _show(context, message, _FeedbackKind.error);

  static void _show(BuildContext context, String message, _FeedbackKind kind) {
    final palette = context.palette;
    final color = kind == _FeedbackKind.success ? palette.success : palette.danger;
    final icon = kind == _FeedbackKind.success
        ? Icons.check_circle_rounded
        : Icons.error_rounded;

    ScaffoldMessenger.of(context)
      ..clearSnackBars()
      ..showSnackBar(
        SnackBar(
          behavior: SnackBarBehavior.floating,
          backgroundColor: palette.surface,
          elevation: 0,
          duration: const Duration(seconds: 3),
          shape: RoundedRectangleBorder(
            borderRadius: AppRadius.brMd,
            side: BorderSide(color: palette.border),
          ),
          content: Row(
            children: [
              Icon(icon, color: color, size: 20),
              const SizedBox(width: 12),
              Expanded(
                child: Text(message,
                    style: AppTypography.bodyMd.copyWith(color: palette.textPrimary)),
              ),
            ],
          ),
        ),
      );
  }
}
