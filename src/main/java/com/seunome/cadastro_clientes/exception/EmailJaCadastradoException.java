package com.seunome.cadastro_clientes.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Email já cadastrado: " + email);
    }
}
