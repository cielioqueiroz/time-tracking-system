/// Spacing scale — a single source of truth for all gaps, paddings and margins.
///
/// Based on a 4pt grid. Never hard-code spacing in widgets; reference these
/// tokens so rhythm stays consistent across the whole product.
abstract final class AppSpacing {
  static const double none = 0;
  static const double xxs = 2;
  static const double xs = 4;
  static const double sm = 8;
  static const double md = 12;
  static const double lg = 16;
  static const double xl = 20;
  static const double xxl = 24;
  static const double xxxl = 32;
  static const double huge = 48;
  static const double giant = 64;
}
