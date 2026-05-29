import 'package:flutter_riverpod/flutter_riverpod.dart';

/// Holds the current JWT access token in memory.
///
/// Single responsibility: be the source of truth for "am I authenticated and
/// with which token". The network layer reads it; the auth bootstrap sets it.
class AuthSessionController extends Notifier<String?> {
  @override
  String? build() => null;

  void setToken(String token) => state = token;

  void clear() => state = null;

  bool get isAuthenticated => state != null;
}

final authSessionProvider =
    NotifierProvider<AuthSessionController, String?>(AuthSessionController.new);
