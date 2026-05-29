import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../core/network/dio_provider.dart';
import '../../../core/services/auth_session_controller.dart';
import '../data/auth_remote_datasource.dart';
import '../data/auth_repository_impl.dart';
import '../domain/auth_repository.dart';

final _authDataSourceProvider = Provider<AuthRemoteDataSource>(
  (ref) => AuthRemoteDataSource(ref.watch(dioProvider)),
);

final authRepositoryProvider = Provider<AuthRepository>(
  (ref) => AuthRepositoryImpl(ref.watch(_authDataSourceProvider)),
);

/// Bootstraps a session at app start.
///
/// The scope has a single admin user and no login screen, so we authenticate
/// with the default credentials and store the token. Swapping this for a real
/// login screen later only touches this provider + a screen.
final sessionBootstrapProvider = FutureProvider<void>((ref) async {
  final repository = ref.watch(authRepositoryProvider);
  final token = await repository.login(username: 'admin', password: 'admin');
  ref.read(authSessionProvider.notifier).setToken(token);
});
