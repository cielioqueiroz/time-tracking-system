import 'package:flutter/material.dart';

/// Elevation / shadow tokens. Subtle, layered shadows for a premium feel
/// (avoid heavy Material drop shadows).
abstract final class AppElevation {
  static const List<BoxShadow> none = [];

  static const List<BoxShadow> sm = [
    BoxShadow(color: Color(0x0F101828), blurRadius: 2, offset: Offset(0, 1)),
    BoxShadow(color: Color(0x14101828), blurRadius: 3, offset: Offset(0, 1)),
  ];

  static const List<BoxShadow> md = [
    BoxShadow(color: Color(0x0F101828), blurRadius: 8, offset: Offset(0, 4)),
    BoxShadow(color: Color(0x14101828), blurRadius: 4, offset: Offset(0, 2)),
  ];

  static const List<BoxShadow> lg = [
    BoxShadow(color: Color(0x14101828), blurRadius: 24, offset: Offset(0, 12)),
    BoxShadow(color: Color(0x0F101828), blurRadius: 8, offset: Offset(0, 4)),
  ];
}
