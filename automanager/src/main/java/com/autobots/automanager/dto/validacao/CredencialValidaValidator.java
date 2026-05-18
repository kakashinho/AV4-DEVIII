package com.autobots.automanager.dto.validacao;

import com.autobots.automanager.dto.requisicao.CredencialRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CredencialValidaValidator implements ConstraintValidator<CredencialValida, CredencialRequest> {

    @Override
    public boolean isValid(CredencialRequest value, ConstraintValidatorContext ctx) {
        if (value == null) return true;

        boolean temNome = value.getNomeUsuario() != null && !value.getNomeUsuario().isBlank();
        boolean temSenha = value.getSenha() != null && !value.getSenha().isBlank();
        boolean temCodigo = value.getCodigo() != null;

        ctx.disableDefaultConstraintViolation();

        // Rejeitar modo misto: nomeUsuario/senha E codigo ao mesmo tempo
        if ((temNome || temSenha) && temCodigo) {
            ctx.buildConstraintViolationWithTemplate(
                    "Usar apenas código OU usuário/senha, não ambos.")
               .addConstraintViolation();
            return false;
        }

        // Modo senha: nomeUsuario obrigatório, senha obrigatória
        if (temNome && !temSenha) {
            ctx.buildConstraintViolationWithTemplate("Senha é obrigatória quando nomeUsuario é informado.")
               .addPropertyNode("senha")
               .addConstraintViolation();
            return false;
        }
        if (temSenha && !temNome) {
            ctx.buildConstraintViolationWithTemplate("Nome de usuário é obrigatório quando senha é informada.")
               .addPropertyNode("nomeUsuario")
               .addConstraintViolation();
            return false;
        }

        // Pelo menos um modo deve ser informado
        if (!temNome && !temSenha && !temCodigo) {
            ctx.buildConstraintViolationWithTemplate(
                    "Informe nomeUsuario + senha (modo senha) ou codigo (modo código).")
               .addConstraintViolation();
            return false;
        }

        return true;
    }
}
