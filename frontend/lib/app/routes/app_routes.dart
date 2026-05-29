/// Centralized route paths and names — no magic strings scattered in widgets.
abstract final class AppRoutes {
  static const String collaborators = '/';
  static const String newCollaborator = '/collaborators/new';

  static String editCollaborator(String id) => '/collaborators/$id/edit';
  static String history(String id) => '/collaborators/$id/history';

  // Path patterns used in the router definition.
  static const String editCollaboratorPattern = '/collaborators/:id/edit';
  static const String historyPattern = '/collaborators/:id/history';

  // Route names.
  static const String collaboratorsName = 'collaborators';
  static const String newCollaboratorName = 'new-collaborator';
  static const String editCollaboratorName = 'edit-collaborator';
  static const String historyName = 'history';
}
