sealed class Failure {
  const Failure(this.message);
  final String message;
}

class NetworkFailure extends Failure {
  const NetworkFailure([super.message = 'Sem conexão com o servidor.']);
}

class ServerFailure extends Failure {
  const ServerFailure([super.message = 'Erro no servidor.']);
}

class NotFoundFailure extends Failure {
  const NotFoundFailure([super.message = 'Recurso não encontrado.']);
}

class ValidationFailure extends Failure {
  const ValidationFailure(super.message, {this.fieldErrors = const {}});
  final Map<String, String> fieldErrors;
}

class UnauthorizedFailure extends Failure {
  const UnauthorizedFailure([super.message = 'Não autorizado.']);
}

class UnknownFailure extends Failure {
  const UnknownFailure([super.message = 'Algo deu errado.']);
}
