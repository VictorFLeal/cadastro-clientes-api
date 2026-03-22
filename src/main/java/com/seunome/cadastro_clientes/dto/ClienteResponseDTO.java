package com.seunome.cadastro_clientes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        String endereco,
        String cidade,
        String estado,
        String cep,
        LocalDateTime dataCadastro
) {
}
