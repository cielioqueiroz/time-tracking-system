import 'package:flutter/material.dart';

import '../foundations/app_theme_extension.dart';
import '../tokens/app_durations.dart';
import '../tokens/app_radius.dart';
import '../tokens/app_spacing.dart';
import '../tokens/app_typography.dart';

class AppTextField extends StatefulWidget {
  const AppTextField({
    super.key,
    required this.label,
    required this.controller,
    this.hint,
    this.errorText,
    this.keyboardType,
    this.autofocus = false,
    this.onChanged,
    this.onSubmitted,
  });

  final String label;
  final TextEditingController controller;
  final String? hint;
  final String? errorText;
  final TextInputType? keyboardType;
  final bool autofocus;
  final ValueChanged<String>? onChanged;
  final ValueChanged<String>? onSubmitted;

  @override
  State<AppTextField> createState() => _AppTextFieldState();
}

class _AppTextFieldState extends State<AppTextField> {
  final _focusNode = FocusNode();
  bool _focused = false;

  @override
  void initState() {
    super.initState();
    _focusNode.addListener(() => setState(() => _focused = _focusNode.hasFocus));
  }

  @override
  void dispose() {
    _focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final palette = context.palette;
    final hasError = widget.errorText != null;
    final borderColor = hasError
        ? palette.danger
        : _focused
            ? palette.accent
            : palette.border;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(widget.label,
            style: AppTypography.label.copyWith(color: palette.textSecondary)),
        const SizedBox(height: AppSpacing.sm),
        AnimatedContainer(
          duration: AppDurations.fast,
          decoration: BoxDecoration(
            color: palette.surface,
            borderRadius: AppRadius.brMd,
            border: Border.all(color: borderColor, width: _focused ? 1.5 : 1),
          ),
          child: TextField(
            controller: widget.controller,
            focusNode: _focusNode,
            autofocus: widget.autofocus,
            keyboardType: widget.keyboardType,
            onChanged: widget.onChanged,
            onSubmitted: widget.onSubmitted,
            style: AppTypography.bodyMd.copyWith(color: palette.textPrimary),
            cursorColor: palette.accent,
            decoration: InputDecoration(
              hintText: widget.hint,
              hintStyle: AppTypography.bodyMd.copyWith(color: palette.textMuted),
              border: InputBorder.none,
              isDense: true,
              contentPadding: const EdgeInsets.symmetric(
                  horizontal: AppSpacing.lg, vertical: 14),
            ),
          ),
        ),
        AnimatedSize(
          duration: AppDurations.fast,
          child: hasError
              ? Padding(
                  padding: const EdgeInsets.only(top: AppSpacing.xs),
                  child: Text(widget.errorText!,
                      style: AppTypography.caption.copyWith(color: palette.danger)),
                )
              : const SizedBox(width: double.infinity),
        ),
      ],
    );
  }
}
