import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/errors/failure.dart';
import '../../../../design_system/atoms/app_button.dart';
import '../../../../design_system/atoms/app_card.dart';
import '../../../../design_system/atoms/app_text_field.dart';
import '../../../../design_system/layouts/app_page.dart';
import '../../../../design_system/molecules/app_feedback.dart';
import '../../../../design_system/tokens/app_spacing.dart';
import '../../application/collaborators_controller.dart';

class CollaboratorFormScreen extends ConsumerStatefulWidget {
  const CollaboratorFormScreen({super.key, this.collaboratorId});

  final String? collaboratorId;

  bool get isEditing => collaboratorId != null;

  @override
  ConsumerState<CollaboratorFormScreen> createState() => _CollaboratorFormScreenState();
}

class _CollaboratorFormScreenState extends ConsumerState<CollaboratorFormScreen> {
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _cargoController = TextEditingController();

  String? _nameError;
  String? _emailError;
  String? _cargoError;
  bool _submitting = false;
  bool _prefilled = false;

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _cargoController.dispose();
    super.dispose();
  }

  void _prefillIfNeeded() {
    if (_prefilled || !widget.isEditing) return;
    final list = ref.read(collaboratorsControllerProvider).valueOrNull;
    final existing = list?.where((c) => c.id == widget.collaboratorId).firstOrNull;
    if (existing != null) {
      _nameController.text = existing.name;
      _emailController.text = existing.email;
      _cargoController.text = existing.cargo;
      _prefilled = true;
    }
  }

  bool _validate() {
    final name = _nameController.text.trim();
    final email = _emailController.text.trim();
    final cargo = _cargoController.text.trim();
    final emailRegex = RegExp(r'^[\w.+-]+@[\w-]+\.[\w.-]+$');
    setState(() {
      _nameError = name.isEmpty ? 'O nome é obrigatório.' : null;
      _emailError = email.isEmpty
          ? 'O e-mail é obrigatório.'
          : (!emailRegex.hasMatch(email) ? 'E-mail inválido.' : null);
      _cargoError = cargo.isEmpty ? 'O cargo é obrigatório.' : null;
    });
    return _nameError == null && _emailError == null && _cargoError == null;
  }

  Future<void> _submit() async {
    if (!_validate()) return;
    setState(() => _submitting = true);

    final controller = ref.read(collaboratorsControllerProvider.notifier);
    final name = _nameController.text.trim();
    final email = _emailController.text.trim();
    final cargo = _cargoController.text.trim();

    try {
      if (widget.isEditing) {
        await controller.edit(
            id: widget.collaboratorId!, name: name, email: email, cargo: cargo);
      } else {
        await controller.create(name: name, email: email, cargo: cargo);
      }
      if (!mounted) return;
      AppFeedback.success(
          context, widget.isEditing ? 'Colaborador atualizado.' : 'Colaborador criado.');
      context.pop();
    } on ValidationFailure catch (f) {
      if (!mounted) return;
      setState(() {
        _nameError = f.fieldErrors['name'] ?? _nameError;
        _emailError = f.fieldErrors['email'] ?? _emailError;
        _cargoError = f.fieldErrors['cargo'] ?? _cargoError;
      });
      AppFeedback.error(context, f.message);
    } on Failure catch (f) {
      if (!mounted) return;
      AppFeedback.error(context, f.message);
    } finally {
      if (mounted) setState(() => _submitting = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    _prefillIfNeeded();

    return AppPage(
      title: widget.isEditing ? 'Editar colaborador' : 'Novo colaborador',
      subtitle: widget.isEditing
          ? 'Atualize os dados do colaborador'
          : 'Cadastre um novo colaborador',
      onBack: () => context.pop(),
      maxContentWidth: 560,
      body: SingleChildScrollView(
        child: AppCard(
          padding: const EdgeInsets.all(AppSpacing.xxl),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              AppTextField(
                label: 'Nome',
                hint: 'Ex.: José Silva',
                controller: _nameController,
                autofocus: true,
                errorText: _nameError,
                onChanged: (_) {
                  if (_nameError != null) setState(() => _nameError = null);
                },
              ),
              const SizedBox(height: AppSpacing.lg),
              AppTextField(
                label: 'E-mail',
                hint: 'Ex.: jose.silva@empresa.com',
                controller: _emailController,
                keyboardType: TextInputType.emailAddress,
                errorText: _emailError,
                onChanged: (_) {
                  if (_emailError != null) setState(() => _emailError = null);
                },
              ),
              const SizedBox(height: AppSpacing.lg),
              AppTextField(
                label: 'Cargo',
                hint: 'Ex.: Desenvolvedor',
                controller: _cargoController,
                errorText: _cargoError,
                onChanged: (_) {
                  if (_cargoError != null) setState(() => _cargoError = null);
                },
                onSubmitted: (_) => _submit(),
              ),
              const SizedBox(height: AppSpacing.xxl),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  AppButton(
                    label: 'Cancelar',
                    variant: AppButtonVariant.ghost,
                    onPressed: _submitting ? null : () => context.pop(),
                  ),
                  const SizedBox(width: AppSpacing.sm),
                  AppButton(
                    label: widget.isEditing ? 'Salvar alterações' : 'Criar colaborador',
                    icon: Icons.check_rounded,
                    loading: _submitting,
                    onPressed: _submit,
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
