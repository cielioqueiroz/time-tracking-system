import 'package:flutter/material.dart';

import '../foundations/app_theme_extension.dart';
import '../tokens/app_spacing.dart';
import '../tokens/app_typography.dart';

class AppPage extends StatelessWidget {
  const AppPage({
    super.key,
    required this.title,
    required this.body,
    this.subtitle,
    this.actions = const [],
    this.onBack,
    this.maxContentWidth = 960,
  });

  final String title;
  final Widget body;
  final String? subtitle;
  final List<Widget> actions;
  final VoidCallback? onBack;
  final double maxContentWidth;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;

    return Scaffold(
      body: Column(
        children: [
          _TopBar(
            title: title,
            subtitle: subtitle,
            actions: actions,
            onBack: onBack,
          ),
          Container(height: 1, color: palette.border),
          Expanded(
            child: Align(
              alignment: Alignment.topCenter,
              child: ConstrainedBox(
                constraints: BoxConstraints(maxWidth: maxContentWidth),
                child: Padding(
                  padding: const EdgeInsets.symmetric(
                      horizontal: AppSpacing.xxl, vertical: AppSpacing.xl),
                  child: body,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _TopBar extends StatelessWidget {
  const _TopBar({
    required this.title,
    required this.subtitle,
    required this.actions,
    required this.onBack,
  });

  final String title;
  final String? subtitle;
  final List<Widget> actions;
  final VoidCallback? onBack;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return SafeArea(
      bottom: false,
      child: Container(
        height: 68,
        padding: const EdgeInsets.symmetric(horizontal: AppSpacing.xxl),
        alignment: Alignment.center,
        child: Align(
          alignment: Alignment.center,
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 960 + AppSpacing.xxl * 2),
            child: Row(
              children: [
                if (onBack != null) ...[
                  _BackButton(onTap: onBack!),
                  const SizedBox(width: AppSpacing.md),
                ],
                Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(title,
                        style: AppTypography.titleMd.copyWith(
                            color: palette.textPrimary, fontWeight: FontWeight.w700)),
                    if (subtitle != null)
                      Text(subtitle!,
                          style: AppTypography.caption.copyWith(color: palette.textMuted)),
                  ],
                ),
                const Spacer(),
                ...actions,
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _BackButton extends StatelessWidget {
  const _BackButton({required this.onTap});
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    return IconButton(
      onPressed: onTap,
      icon: Icon(Icons.arrow_back_rounded, color: palette.textSecondary, size: 20),
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
