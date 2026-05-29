import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/network/dio_provider.dart';
import '../data/datasources/collaborator_remote_datasource.dart';
import '../data/repositories/collaborator_repository_impl.dart';
import '../domain/repositories/collaborator_repository.dart';

final _collaboratorDataSourceProvider = Provider<CollaboratorRemoteDataSource>(
  (ref) => CollaboratorRemoteDataSource(ref.watch(dioProvider)),
);

final collaboratorRepositoryProvider = Provider<CollaboratorRepository>(
  (ref) => CollaboratorRepositoryImpl(ref.watch(_collaboratorDataSourceProvider)),
);
