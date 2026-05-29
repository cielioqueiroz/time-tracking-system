import 'package:dio/dio.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/core/errors/failure.dart';
import 'package:time_tracking/app/core/network/error_mapper.dart';

DioException _dioWithType(DioExceptionType type) {
  return DioException(
    requestOptions: RequestOptions(path: '/x'),
    type: type,
  );
}

DioException _dioWithResponse(int status, dynamic data) {
  final options = RequestOptions(path: '/x');
  return DioException(
    requestOptions: options,
    type: DioExceptionType.badResponse,
    response: Response<dynamic>(
      requestOptions: options,
      statusCode: status,
      data: data,
    ),
  );
}

Map<String, dynamic> _envelope(List<Map<String, dynamic>> errors) => {
      'success': false,
      'data': null,
      'errors': errors,
    };

void main() {
  test('non-Dio error maps to UnknownFailure', () {
    expect(mapError(Exception('boom')), isA<UnknownFailure>());
  });

  group('transport errors', () {
    test('timeouts and connection errors map to NetworkFailure', () {
      expect(mapError(_dioWithType(DioExceptionType.connectionTimeout)),
          isA<NetworkFailure>());
      expect(mapError(_dioWithType(DioExceptionType.sendTimeout)),
          isA<NetworkFailure>());
      expect(mapError(_dioWithType(DioExceptionType.receiveTimeout)),
          isA<NetworkFailure>());
      expect(mapError(_dioWithType(DioExceptionType.connectionError)),
          isA<NetworkFailure>());
    });

    test('cancel / badCertificate / unknown map to UnknownFailure', () {
      expect(mapError(_dioWithType(DioExceptionType.cancel)),
          isA<UnknownFailure>());
      expect(mapError(_dioWithType(DioExceptionType.badCertificate)),
          isA<UnknownFailure>());
      expect(mapError(_dioWithType(DioExceptionType.unknown)),
          isA<UnknownFailure>());
    });
  });

  group('bad response status mapping', () {
    test('400 → ValidationFailure with field errors and message', () {
      final failure = mapError(_dioWithResponse(
        400,
        _envelope([
          {'code': 'VALIDATION', 'message': 'E-mail inválido.', 'field': 'email'},
        ]),
      ));

      expect(failure, isA<ValidationFailure>());
      failure as ValidationFailure;
      expect(failure.message, 'E-mail inválido.');
      expect(failure.fieldErrors, {'email': 'E-mail inválido.'});
    });

    test('401 → UnauthorizedFailure', () {
      expect(mapError(_dioWithResponse(401, null)), isA<UnauthorizedFailure>());
    });

    test('403 → UnauthorizedFailure', () {
      expect(mapError(_dioWithResponse(403, null)), isA<UnauthorizedFailure>());
    });

    test('404 → NotFoundFailure with server message', () {
      final failure = mapError(_dioWithResponse(
        404,
        _envelope([
          {'code': 'NOT_FOUND', 'message': 'Colaborador não encontrado.'},
        ]),
      ));

      expect(failure, isA<NotFoundFailure>());
      expect(failure.message, 'Colaborador não encontrado.');
    });

    test('409 → ValidationFailure', () {
      final failure = mapError(_dioWithResponse(
        409,
        _envelope([
          {'code': 'CONFLICT', 'message': 'E-mail já cadastrado.'},
        ]),
      ));

      expect(failure, isA<ValidationFailure>());
      expect(failure.message, 'E-mail já cadastrado.');
    });

    test('500 → ServerFailure', () {
      expect(mapError(_dioWithResponse(500, null)), isA<ServerFailure>());
    });

    test('unmapped status falls back to UnknownFailure', () {
      expect(mapError(_dioWithResponse(418, null)), isA<UnknownFailure>());
    });
  });
}
