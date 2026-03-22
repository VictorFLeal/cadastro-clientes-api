package com.seunome.cadastro_clientes.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado com id: " + id);
    }
}
