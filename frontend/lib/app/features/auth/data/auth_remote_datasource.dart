import 'package:dio/dio.dart';

import '../../../core/constants/api_constants.dart';

/// Talks to the auth endpoints. The ONLY place auth HTTP calls live.
class AuthRemoteDataSource {
  AuthRemoteDataSource(this._dio);

  final Dio _dio;

  /// POST /auth/login → returns the access token from the API envelope.
  Future<String> login(String username, String password) async {
    final response = await _dio.post<Map<String, dynamic>>(
      ApiConstants.authLogin,
      data: {'username': username, 'password': password},
    );
    final data = response.data!['data'] as Map<String, dynamic>;
    return data['accessToken'] as String;
  }
}
