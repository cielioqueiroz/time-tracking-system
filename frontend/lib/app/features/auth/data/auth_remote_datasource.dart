import 'package:dio/dio.dart';

import '../../../core/constants/api_constants.dart';

class AuthRemoteDataSource {
  AuthRemoteDataSource(this._dio);

  final Dio _dio;

  Future<String> login(String username, String password) async {
    final response = await _dio.post<Map<String, dynamic>>(
      ApiConstants.authLogin,
      data: {'username': username, 'password': password},
    );
    final data = response.data!['data'] as Map<String, dynamic>;
    return data['accessToken'] as String;
  }
}
