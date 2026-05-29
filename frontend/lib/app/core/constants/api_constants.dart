/// Centralized API configuration constants.
///
/// The base URL is overridable at build time:
/// `flutter run --dart-define=API_BASE_URL=http://10.0.2.2:8080`
abstract final class ApiConstants {
  static const String baseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'http://localhost:8080',
  );

  static const Duration connectTimeout = Duration(seconds: 15);
  static const Duration receiveTimeout = Duration(seconds: 15);

  // Endpoints (filled in as features land in ETAPA 4).
  static const String collaborators = '/api/v1/collaborators';
  static const String workSessions = '/api/v1/work-sessions';
  static const String authLogin = '/api/v1/auth/login';
}
