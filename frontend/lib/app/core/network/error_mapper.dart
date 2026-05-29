import 'package:dio/dio.dart';

import '../errors/failure.dart';

Failure mapError(Object error) {
  if (error is! DioException) {
    return const UnknownFailure();
  }

  switch (error.type) {
    case DioExceptionType.connectionTimeout:
    case DioExceptionType.sendTimeout:
    case DioExceptionType.receiveTimeout:
    case DioExceptionType.connectionError:
      return const NetworkFailure();
    case DioExceptionType.badResponse:
      return _mapResponse(error.response);
    case DioExceptionType.cancel:
    case DioExceptionType.badCertificate:
    case DioExceptionType.unknown:
      return const UnknownFailure();
  }
}

Failure _mapResponse(Response<dynamic>? response) {
  final status = response?.statusCode ?? 0;
  final message = _extractMessage(response?.data);
  final fieldErrors = _extractFieldErrors(response?.data);

  return switch (status) {
    400 => ValidationFailure(message ?? 'Dados inválidos.', fieldErrors: fieldErrors),
    401 || 403 => UnauthorizedFailure(message ?? 'Não autorizado.'),
    404 => NotFoundFailure(message ?? 'Recurso não encontrado.'),
    409 => ValidationFailure(message ?? 'Conflito com o estado atual.'),
    >= 500 => ServerFailure(message ?? 'Erro no servidor.'),
    _ => UnknownFailure(message ?? 'Algo deu errado.'),
  };
}

String? _extractMessage(dynamic data) {
  if (data is Map && data['errors'] is List && (data['errors'] as List).isNotEmpty) {
    final first = (data['errors'] as List).first;
    if (first is Map && first['message'] is String) {
      return first['message'] as String;
    }
  }
  return null;
}

Map<String, String> _extractFieldErrors(dynamic data) {
  final result = <String, String>{};
  if (data is Map && data['errors'] is List) {
    for (final e in data['errors'] as List) {
      if (e is Map && e['field'] is String && e['message'] is String) {
        result[e['field'] as String] = e['message'] as String;
      }
    }
  }
  return result;
}
