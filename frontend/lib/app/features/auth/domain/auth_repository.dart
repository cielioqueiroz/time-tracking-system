/// Domain contract for authentication. Implemented in the data layer.
abstract interface class AuthRepository {
  /// Authenticates and returns a JWT access token, or throws a `Failure`.
  Future<String> login({required String username, required String password});
}
