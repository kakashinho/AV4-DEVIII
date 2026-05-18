package com.autobots.automanager.dto.validacao;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CredencialValidaValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CredencialValida {

    String message() default "Credencial inválida: informe nomeUsuario+senha ou codigo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
