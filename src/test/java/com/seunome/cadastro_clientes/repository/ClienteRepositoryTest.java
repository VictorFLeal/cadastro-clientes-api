package com.seunome.cadastro_clientes.repository;

import com.seunome.cadastro_clientes.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// @DataJpaTest sobe apenas a camada JPA (sem controllers, sem services)
// Usa o banco H2 em memória definido em src/test/resources/application.properties
@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    private Cliente clienteBase;

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Limpa entre testes
        clienteBase = criarCliente("Victor Leal", "52998224725", "victor@email.com");
        repository.save(clienteBase);
    }

    @Nested
    @DisplayName("existsByCpf()")
    class ExistsByCpf {

        @Test
        @DisplayName("deve retornar true quando CPF existe")
        void deveRetornarTrue_quandoCpfExiste() {
            assertThat(repository.existsByCpf("52998224725")).isTrue();
        }

        @Test
        @DisplayName("deve retornar false quando CPF não existe")
        void deveRetornarFalse_quandoCpfNaoExiste() {
            assertThat(repository.existsByCpf("00000000000")).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByEmail()")
    class ExistsByEmail {

        @Test
        @DisplayName("deve retornar true quando email existe")
        void deveRetornarTrue_quandoEmailExiste() {
            assertThat(repository.existsByEmail("victor@email.com")).isTrue();
        }

        @Test
        @DisplayName("deve retornar false quando email não existe")
        void deveRetornarFalse_quandoEmailNaoExiste() {
            assertThat(repository.existsByEmail("outro@email.com")).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCpf()")
    class FindByCpf {

        @Test
        @DisplayName("deve encontrar cliente pelo CPF")
        void deveEncontrarCliente_quandoCpfExiste() {
            Optional<Cliente> resultado = repository.findByCpf("52998224725");

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNome()).isEqualTo("Victor Leal");
        }

        @Test
        @DisplayName("deve retornar vazio quando CPF não existe")
        void deveRetornarVazio_quandoCpfNaoExiste() {
            Optional<Cliente> resultado = repository.findByCpf("00000000000");
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByNomeContainingIgnoreCase()")
    class BuscarPorNome {

        @BeforeEach
        void adicionaMaisClientes() {
            repository.save(criarCliente("Ana Costa", "11144477735", "ana@email.com"));
            repository.save(criarCliente("João Victor", "11144477700", "joao@email.com"));
        }

        @Test
        @DisplayName("deve encontrar clientes por trecho do nome (case insensitive)")
        void deveEncontrarPorTrechoDoNome() {
            Page<Cliente> resultado = repository.findByNomeContainingIgnoreCase(
                    "victor", PageRequest.of(0, 10)
            );

            // Espera 2 clientes: "Victor Leal" e "João Victor"
            assertThat(resultado.getTotalElements()).isEqualTo(2);
            assertThat(resultado.getContent())
                    .extracting(Cliente::getNome)
                    .containsExactlyInAnyOrder("Victor Leal", "João Victor");
        }

        @Test
        @DisplayName("deve retornar vazio quando nenhum nome corresponde")
        void deveRetornarVazio_quandoNomeSemCorrespondencia() {
            Page<Cliente> resultado = repository.findByNomeContainingIgnoreCase(
                    "zzznome", PageRequest.of(0, 10)
            );
            assertThat(resultado.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("constraint UNIQUE no banco")
    class ConstraintUnique {

        @Test
        @DisplayName("não deve permitir dois clientes com o mesmo CPF")
        void deveRejeitarCpfDuplicado() {
            Cliente duplicado = criarCliente("Outro Nome", "52998224725", "outro@email.com");

            // A constraint UNIQUE do banco lança exceção ao fazer flush
            assertThatThrownBy(() -> {
                repository.save(duplicado);
                repository.flush(); // força o SQL para o banco
            });
        }

        @Test
        @DisplayName("não deve permitir dois clientes com o mesmo email")
        void deveRejeitarEmailDuplicado() {
            Cliente duplicado = criarCliente("Outro Nome", "11144477735", "victor@email.com");

            assertThatThrownBy(() -> {
                repository.save(duplicado);
                repository.flush();
            });
        }
    }

    // -------------------------------------------------------
    // Método auxiliar para criar entidades nos testes
    // -------------------------------------------------------
    private Cliente criarCliente(String nome, String cpf, String email) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setCpf(cpf);
        c.setEmail(email);
        c.setDataNascimento(LocalDate.of(1995, 1, 15));
        return c;
    }
}
