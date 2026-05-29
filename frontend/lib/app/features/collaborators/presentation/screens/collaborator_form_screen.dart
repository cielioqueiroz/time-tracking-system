import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// Create/Edit collaborator screen. Thin placeholder for ETAPA 4; the polished
/// form with elegant validation/feedback is built in ETAPA 5.
class CollaboratorFormScreen extends ConsumerWidget {
  const CollaboratorFormScreen({super.key, this.collaboratorId});

  /// Null = create mode; non-null = edit mode.
  final String? collaboratorId;

  bool get isEditing => collaboratorId != null;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      appBar: AppBar(title: Text(isEditing ? 'Editar colaborador' : 'Novo colaborador')),
      body: const Center(child: Text('Formulário — em construção (ETAPA 5)')),
    );
  }
}
