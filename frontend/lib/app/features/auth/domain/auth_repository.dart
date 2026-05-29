abstract interface class AuthRepository {
  Future<String> login({required String username, required String password});
}
