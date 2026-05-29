import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

abstract final class AppFonts {
  static TextStyle sans(TextStyle base) => GoogleFonts.manrope(textStyle: base);

  static TextStyle mono(TextStyle base) =>
      GoogleFonts.jetBrainsMono(textStyle: base);

  static TextTheme sansTextTheme(TextTheme base) =>
      GoogleFonts.manropeTextTheme(base);
}
