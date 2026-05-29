import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

/// Font helpers — the single place the actual typefaces are bound.
///
/// Sans (Manrope) for all UI text; Mono (JetBrains Mono) for time data
/// (durations, timestamps, ids) — a deliberate, on-theme touch for a time
/// tracking product.
abstract final class AppFonts {
  static TextStyle sans(TextStyle base) => GoogleFonts.manrope(textStyle: base);

  static TextStyle mono(TextStyle base) =>
      GoogleFonts.jetBrainsMono(textStyle: base);

  static TextTheme sansTextTheme(TextTheme base) =>
      GoogleFonts.manropeTextTheme(base);
}
