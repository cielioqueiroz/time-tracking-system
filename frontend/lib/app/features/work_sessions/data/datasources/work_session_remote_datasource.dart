import 'package:dio/dio.dart';

import '../../../../core/constants/api_constants.dart';
import '../../domain/entities/work_session.dart';
import '../../domain/entities/work_summary.dart';
import '../models/work_session_model.dart';
import '../models/work_summary_model.dart';

class WorkSessionRemoteDataSource {
  WorkSessionRemoteDataSource(this._dio);

  final Dio _dio;

  String _basePath(String collaboratorId) =>
      '${ApiConstants.collaborators}/$collaboratorId/work-sessions';

  Future<WorkSession> start(String collaboratorId) async {
    final response = await _dio.post<Map<String, dynamic>>(
      '${_basePath(collaboratorId)}/start',
    );
    return WorkSessionModel.fromJson(
        response.data!['data'] as Map<String, dynamic>);
  }

  Future<WorkSession> finish(String collaboratorId) async {
    final response = await _dio.post<Map<String, dynamic>>(
      '${_basePath(collaboratorId)}/finish',
    );
    return WorkSessionModel.fromJson(
        response.data!['data'] as Map<String, dynamic>);
  }

  Future<List<WorkSession>> history(
    String collaboratorId, {
    required int page,
    required int size,
  }) async {
    final response = await _dio.get<Map<String, dynamic>>(
      _basePath(collaboratorId),
      queryParameters: {'page': page, 'size': size},
    );
    final data = response.data!['data'] as Map<String, dynamic>;
    final content = data['content'] as List<dynamic>;
    return content
        .map((e) => WorkSessionModel.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<WorkSummary> summary(String collaboratorId) async {
    final response = await _dio.get<Map<String, dynamic>>(
      '${_basePath(collaboratorId)}/summary',
    );
    return WorkSummaryModel.fromJson(
        response.data!['data'] as Map<String, dynamic>);
  }

  Future<String> exportCsv(String collaboratorId) async {
    final response = await _dio.get<String>(
      '${_basePath(collaboratorId)}/export',
      options: Options(responseType: ResponseType.plain),
    );
    return response.data ?? '';
  }
}
