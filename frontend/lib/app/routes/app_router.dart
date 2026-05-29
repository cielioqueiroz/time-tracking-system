import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../features/collaborators/presentation/screens/collaborator_form_screen.dart';
import '../features/collaborators/presentation/screens/collaborators_screen.dart';
import '../features/work_sessions/presentation/screens/work_session_history_screen.dart';
import 'app_routes.dart';

final appRouterProvider = Provider<GoRouter>((ref) {
  return GoRouter(
    initialLocation: AppRoutes.collaborators,
    routes: [
      GoRoute(
        path: AppRoutes.collaborators,
        name: AppRoutes.collaboratorsName,
        builder: (context, state) => const CollaboratorsScreen(),
      ),
      GoRoute(
        path: AppRoutes.newCollaborator,
        name: AppRoutes.newCollaboratorName,
        builder: (context, state) => const CollaboratorFormScreen(),
      ),
      GoRoute(
        path: AppRoutes.editCollaboratorPattern,
        name: AppRoutes.editCollaboratorName,
        builder: (context, state) =>
            CollaboratorFormScreen(collaboratorId: state.pathParameters['id']),
      ),
      GoRoute(
        path: AppRoutes.historyPattern,
        name: AppRoutes.historyName,
        builder: (context, state) =>
            WorkSessionHistoryScreen(collaboratorId: state.pathParameters['id']!),
      ),
    ],
  );
});
