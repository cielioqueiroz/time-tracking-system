import 'package:flutter_riverpod/flutter_riverpod.dart';

class AuthSessionController extends Notifier<String?> {
  @override
  String? build() => null;

  void setToken(String token) => state = token;

  void clear() => state = null;

  bool get isAuthenticated => state != null;
}

final authSessionProvider =
    NotifierProvider<AuthSessionController, String?>(AuthSessionController.new);
