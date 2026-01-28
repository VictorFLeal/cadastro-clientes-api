package com.seunome.cadastro_clientes.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})  // Aplica a anotação apenas em campos
@Retention(RetentionPolicy.RUNTIME)  // Disponível em tempo de execução
@Constraint(validatedBy = CpfValidator.class)  // A classe CpfValidator será usada para validar o CPF
public @interface ValidCPF {

    String message() default "CPF inválido";  // Mensagem padrão

    Class<?>[] groups() default {};  // Para validar grupos, caso necessário

    Class<? extends Payload>[] payload() default {};  // Para transportar metadados adicionais
}
