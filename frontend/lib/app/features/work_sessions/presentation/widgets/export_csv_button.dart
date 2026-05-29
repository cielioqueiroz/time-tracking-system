import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../../core/errors/failure.dart';
import '../../../../core/utils/file_download.dart';
import '../../../../design_system/atoms/app_button.dart';
import '../../../../design_system/molecules/app_feedback.dart';
import '../../application/work_session_providers.dart';

/// Top-bar action that exports a collaborator's sessions as a CSV download.
/// Manages its own loading state so the screen stays a simple consumer.
class ExportCsvButton extends ConsumerStatefulWidget {
  const ExportCsvButton({super.key, required this.collaboratorId});

  final String collaboratorId;

  @override
  ConsumerState<ExportCsvButton> createState() => _ExportCsvButtonState();
}

class _ExportCsvButtonState extends ConsumerState<ExportCsvButton> {
  bool _loading = false;

  Future<void> _export() async {
    setState(() => _loading = true);
    try {
      final csv = await ref
          .read(workSessionRepositoryProvider)
          .exportCsv(widget.collaboratorId);
      downloadTextFile(csv, 'jornadas-${widget.collaboratorId}.csv');
      if (mounted) AppFeedback.success(context, 'CSV exportado com sucesso.');
    } on Failure catch (f) {
      if (mounted) AppFeedback.error(context, f.message);
    } catch (_) {
      if (mounted) AppFeedback.error(context, 'Não foi possível exportar o CSV.');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return AppButton(
      label: 'Exportar CSV',
      icon: Icons.download_rounded,
      variant: AppButtonVariant.secondary,
      size: AppButtonSize.sm,
      loading: _loading,
      onPressed: _export,
    );
  }
}
