import '../../../core/network/error_mapper.dart';
import '../domain/auth_repository.dart';
import 'auth_remote_datasource.dart';

/// Data-layer implementation of [AuthRepository].
class AuthRepositoryImpl implements AuthRepository {
  AuthRepositoryImpl(this._remote);

  final AuthRemoteDataSource _remote;

  @override
  Future<String> login({required String username, required String password}) async {
    try {
      return await _remote.login(username, password);
    } catch (e) {
      throw mapError(e);
    }
  }
}
