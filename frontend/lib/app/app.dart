import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'core/theme/theme_mode_controller.dart';
import 'design_system/foundations/app_theme.dart';

/// Root application widget.
///
/// Wires the design-system themes and the active [ThemeMode]. The router is
/// introduced in ETAPA 4; for now a placeholder home confirms the base boots.
class TimeTrackingApp extends ConsumerWidget {
  const TimeTrackingApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final themeMode = ref.watch(themeModeProvider);

    return MaterialApp(
      title: 'Time Tracking',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light(),
      darkTheme: AppTheme.dark(),
      themeMode: themeMode,
      home: const _BaseBootScreen(),
    );
  }
}

/// Temporary landing screen (ETAPA 1). Replaced by routed features in ETAPA 4.
class _BaseBootScreen extends StatelessWidget {
  const _BaseBootScreen();

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(child: Text('Time Tracking — base ready')),
    );
  }
}
