package com.seunome.cadastro_clientes.dto;

import com.seunome.cadastro_clientes.validation.ValidCPF;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @ValidCPF
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String telefone,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        String endereco,
        String cidade,
        String estado,

        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
        String cep
) {
    public String getCpf() {
        return "";
    }

    public String getEmail() {
        return "";
    }
}
