import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:time_tracking/app/design_system/atoms/app_button.dart';
import 'package:time_tracking/app/design_system/atoms/status_badge.dart';
import 'package:time_tracking/app/design_system/foundations/app_theme_extension.dart';
import 'package:time_tracking/app/design_system/tokens/app_colors.dart';

Widget _harness(Widget child) => MaterialApp(
      theme: ThemeData(extensions: const [AppThemeExt(palette: AppPalette.light)]),
      home: Scaffold(body: Center(child: child)),
    );

void main() {
  testWidgets('AppButton renders its label and fires onPressed', (tester) async {
    var tapped = false;
    await tester.pumpWidget(_harness(
      AppButton(label: 'Salvar', onPressed: () => tapped = true),
    ));

    expect(find.text('Salvar'), findsOneWidget);
    await tester.tap(find.text('Salvar'));
    expect(tapped, isTrue);
  });

  testWidgets('AppButton does not fire when loading', (tester) async {
    var tapped = false;
    await tester.pumpWidget(_harness(
      AppButton(label: 'Salvar', loading: true, onPressed: () => tapped = true),
    ));

    await tester.tap(find.byType(AppButton));
    expect(tapped, isFalse);
  });

  testWidgets('StatusBadge shows its label', (tester) async {
    await tester.pumpWidget(_harness(
      const StatusBadge(label: 'Trabalhando', color: Color(0xFF1A9D5A)),
    ));

    expect(find.text('Trabalhando'), findsOneWidget);
  });
}
