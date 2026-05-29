/// Domain-friendly error model used across layers instead of raw exceptions.
///
/// Repositories translate transport/exception details into a [Failure];
/// providers map a [Failure] into UI state. The presentation layer never sees
/// `DioException` or stack traces.
sealed class Failure {
  const Failure(this.message);
  final String message;
}

/// No connectivity / timeout / unreachable server.
class NetworkFailure extends Failure {
  const NetworkFailure([super.message = 'Sem conexão com o servidor.']);
}

/// Server responded with a 5xx or unexpected error.
class ServerFailure extends Failure {
  const ServerFailure([super.message = 'Erro no servidor.']);
}

/// Requested resource was not found (404).
class NotFoundFailure extends Failure {
  const NotFoundFailure([super.message = 'Recurso não encontrado.']);
}

/// Validation / business-rule violation (4xx with error payload).
class ValidationFailure extends Failure {
  const ValidationFailure(super.message, {this.fieldErrors = const {}});
  final Map<String, String> fieldErrors;
}

/// Authentication / authorization error (401/403).
class UnauthorizedFailure extends Failure {
  const UnauthorizedFailure([super.message = 'Não autorizado.']);
}

/// Fallback for anything unmapped.
class UnknownFailure extends Failure {
  const UnknownFailure([super.message = 'Algo deu errado.']);
}
