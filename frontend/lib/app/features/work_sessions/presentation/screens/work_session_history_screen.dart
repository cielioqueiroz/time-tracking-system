import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// Work-session history screen. Thin placeholder for ETAPA 4; the elegant
/// timeline is built in ETAPA 5.
class WorkSessionHistoryScreen extends ConsumerWidget {
  const WorkSessionHistoryScreen({super.key, required this.collaboratorId});

  final String collaboratorId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Scaffold(
      appBar: AppBar(title: const Text('Histórico de jornadas')),
      body: const Center(child: Text('Timeline — em construção (ETAPA 5)')),
    );
  }
}
