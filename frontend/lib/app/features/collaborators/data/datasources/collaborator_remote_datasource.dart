import 'package:dio/dio.dart';

import '../../../../core/constants/api_constants.dart';
import '../../domain/entities/collaborator.dart';
import '../models/collaborator_model.dart';

class CollaboratorRemoteDataSource {
  CollaboratorRemoteDataSource(this._dio);

  final Dio _dio;

  Future<List<Collaborator>> list({required int page, required int size}) async {
    final response = await _dio.get<Map<String, dynamic>>(
      ApiConstants.collaborators,
      queryParameters: {'page': page, 'size': size},
    );
    final data = response.data!['data'] as Map<String, dynamic>;
    final content = data['content'] as List<dynamic>;
    return content
        .map((e) => CollaboratorModel.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<Collaborator> create({
    required String name,
    required String email,
    required String cargo,
  }) async {
    final response = await _dio.post<Map<String, dynamic>>(
      ApiConstants.collaborators,
      data: CollaboratorModel.toWriteJson(name: name, email: email, cargo: cargo),
    );
    return CollaboratorModel.fromJson(
        response.data!['data'] as Map<String, dynamic>);
  }

  Future<Collaborator> update({
    required String id,
    required String name,
    required String email,
    required String cargo,
  }) async {
    final response = await _dio.put<Map<String, dynamic>>(
      '${ApiConstants.collaborators}/$id',
      data: CollaboratorModel.toWriteJson(name: name, email: email, cargo: cargo),
    );
    return CollaboratorModel.fromJson(
        response.data!['data'] as Map<String, dynamic>);
  }

  Future<void> delete(String id) async {
    await _dio.delete<void>('${ApiConstants.collaborators}/$id');
  }
}
