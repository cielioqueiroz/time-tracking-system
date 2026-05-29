import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'core/theme/theme_mode_controller.dart';
import 'design_system/foundations/app_theme.dart';
import 'features/auth/application/auth_providers.dart';
import 'routes/app_router.dart';

/// Root application widget. Wires themes, the router, and gates the UI behind
/// the session bootstrap (auth) so authenticated requests are guaranteed.
class TimeTrackingApp extends ConsumerWidget {
  const TimeTrackingApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final themeMode = ref.watch(themeModeProvider);
    final router = ref.watch(appRouterProvider);

    return MaterialApp.router(
      title: 'Time Tracking',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light(),
      darkTheme: AppTheme.dark(),
      themeMode: themeMode,
      routerConfig: router,
      builder: (context, child) => _BootstrapGate(child: child ?? const SizedBox()),
    );
  }
}

/// Shows a splash while authenticating and an error+retry state on failure;
/// otherwise renders the routed app.
class _BootstrapGate extends ConsumerWidget {
  const _BootstrapGate({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final bootstrap = ref.watch(sessionBootstrapProvider);

    return bootstrap.when(
      data: (_) => child,
      loading: () => const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      ),
      error: (error, _) => Scaffold(
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text('Não foi possível conectar ao servidor.'),
              const SizedBox(height: 12),
              FilledButton(
                onPressed: () => ref.invalidate(sessionBootstrapProvider),
                child: const Text('Tentar novamente'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
