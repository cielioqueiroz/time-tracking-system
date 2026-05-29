import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// Collaborators list screen. ETAPA 4 ships a thin placeholder; the premium
/// UI (list, status, actions, loading/empty/error states) lands in ETAPA 5.
class CollaboratorsScreen extends ConsumerWidget {
  const CollaboratorsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      appBar: AppBar(title: const Text('Colaboradores')),
      body: const Center(child: Text('Collaborators — em construção (ETAPA 5)')),
    );
  }
}
