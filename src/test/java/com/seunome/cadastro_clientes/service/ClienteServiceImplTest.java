package com.seunome.cadastro_clientes.service;

import com.seunome.cadastro_clientes.dto.ClienteRequestDTO;
import com.seunome.cadastro_clientes.dto.ClienteResponseDTO;
import com.seunome.cadastro_clientes.entity.Cliente;
import com.seunome.cadastro_clientes.exception.ClienteNotFoundException;
import com.seunome.cadastro_clientes.exception.CpfJaCadastradoException;
import com.seunome.cadastro_clientes.exception.EmailJaCadastradoException;
import com.seunome.cadastro_clientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith inicializa os mocks automaticamente sem precisar de Spring
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    // @Mock cria um "dublê" do repositório — não acessa banco de verdade
    @Mock
    private ClienteRepository repository;

    // @InjectMocks cria a implementação real e injeta os mocks acima
    @InjectMocks
    private ClienteServiceImpl service;

    // Objetos reutilizados em vários testes
    private ClienteRequestDTO requestValido;
    private Cliente clienteSalvo;

    @BeforeEach
    void setUp() {
        // DTO de entrada usado nos testes de criação e atualização
        requestValido = new ClienteRequestDTO(
                "Victor Leal",
                "52998224725", // CPF válido real
                "victor@email.com",
                "51999990000",
                LocalDate.of(2000, 5, 20),
                "Rua das Flores, 100",
                "Porto Alegre",
                "RS",
                "90010000"
        );

        // Entidade que simula o que o banco retornaria após salvar
        clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("Victor Leal");
        clienteSalvo.setCpf("52998224725");
        clienteSalvo.setEmail("victor@email.com");
        clienteSalvo.setTelefone("51999990000");
        clienteSalvo.setDataNascimento(LocalDate.of(2000, 5, 20));
        clienteSalvo.setEndereco("Rua das Flores, 100");
        clienteSalvo.setCidade("Porto Alegre");
        clienteSalvo.setEstado("RS");
        clienteSalvo.setCep("90010000");
        clienteSalvo.setDataCadastro(LocalDateTime.now());
    }

    // ===================================================
    // Agrupa testes por método com @Nested — mais legível
    // ===================================================

    @Nested
    @DisplayName("criar()")
    class Criar {

        @Test
        @DisplayName("deve criar cliente quando CPF e email são inéditos")
        void deveCriarCliente_quandoDadosValidos() {
            // ARRANGE — configura o comportamento dos mocks
            when(repository.existsByCpf("52998224725")).thenReturn(false);
            when(repository.existsByEmail("victor@email.com")).thenReturn(false);
            when(repository.save(any(Cliente.class))).thenReturn(clienteSalvo);

            // ACT — executa o método que está sendo testado
            ClienteResponseDTO resposta = service.criar(requestValido);

            // ASSERT — verifica o resultado
            assertThat(resposta).isNotNull();
            assertThat(resposta.id()).isEqualTo(1L);
            assertThat(resposta.nome()).isEqualTo("Victor Leal");
            assertThat(resposta.email()).isEqualTo("victor@email.com");

            // Verifica que save foi chamado exatamente uma vez
            verify(repository, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("deve lançar CpfJaCadastradoException quando CPF já existe")
        void deveLancarExcecao_quandoCpfJaCadastrado() {
            // ARRANGE — CPF já existe no banco
            when(repository.existsByCpf("52998224725")).thenReturn(true);

            // ASSERT + ACT — assertThatThrownBy captura a exceção esperada
            assertThatThrownBy(() -> service.criar(requestValido))
                    .isInstanceOf(CpfJaCadastradoException.class)
                    .hasMessageContaining("52998224725");

            // Garante que save NUNCA foi chamado (sem efeito colateral)
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar EmailJaCadastradoException quando email já existe")
        void deveLancarExcecao_quandoEmailJaCadastrado() {
            when(repository.existsByCpf("52998224725")).thenReturn(false);
            when(repository.existsByEmail("victor@email.com")).thenReturn(true);

            assertThatThrownBy(() -> service.criar(requestValido))
                    .isInstanceOf(EmailJaCadastradoException.class)
                    .hasMessageContaining("victor@email.com");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar cliente quando id existe")
        void deveRetornarCliente_quandoIdExiste() {
            when(repository.findById(1L)).thenReturn(Optional.of(clienteSalvo));

            ClienteResponseDTO resposta = service.buscarPorId(1L);

            assertThat(resposta.id()).isEqualTo(1L);
            assertThat(resposta.nome()).isEqualTo("Victor Leal");
        }

        @Test
        @DisplayName("deve lançar ClienteNotFoundException quando id não existe")
        void deveLancarExcecao_quandoIdNaoExiste() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscarPorId(99L))
                    .isInstanceOf(ClienteNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("listarTodos()")
    class ListarTodos {

        @Test
        @DisplayName("deve retornar página com clientes cadastrados")
        void deveRetornarPagina_quandoExistemClientes() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
            Page<Cliente> paginaMock = new PageImpl<>(List.of(clienteSalvo), pageable, 1);

            when(repository.findAll(pageable)).thenReturn(paginaMock);

            Page<ClienteResponseDTO> resultado = service.listarTodos(pageable);

            assertThat(resultado.getTotalElements()).isEqualTo(1);
            assertThat(resultado.getContent().get(0).nome()).isEqualTo("Victor Leal");
        }

        @Test
        @DisplayName("deve retornar página vazia quando não há clientes")
        void deveRetornarPaginaVazia_quandoNaoHaClientes() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Cliente> paginaVazia = Page.empty(pageable);

            when(repository.findAll(pageable)).thenReturn(paginaVazia);

            Page<ClienteResponseDTO> resultado = service.listarTodos(pageable);

            assertThat(resultado.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("atualizar()")
    class Atualizar {

        @Test
        @DisplayName("deve atualizar cliente quando id existe")
        void deveAtualizarCliente_quandoIdExiste() {
            // Simula o cliente encontrado no banco
            when(repository.findById(1L)).thenReturn(Optional.of(clienteSalvo));

            // Simula o retorno após o save com os novos dados
            Cliente clienteAtualizado = new Cliente();
            clienteAtualizado.setId(1L);
            clienteAtualizado.setNome("Victor Leal Atualizado");
            clienteAtualizado.setEmail("novoemail@email.com");
            clienteAtualizado.setCpf("52998224725");
            clienteAtualizado.setDataCadastro(LocalDateTime.now());
            clienteAtualizado.setDataNascimento(LocalDate.of(2000, 5, 20));

            when(repository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

            ClienteRequestDTO requestAtualizado = new ClienteRequestDTO(
                    "Victor Leal Atualizado",
                    "52998224725",
                    "novoemail@email.com",
                    "51988887777",
                    LocalDate.of(2000, 5, 20),
                    "Nova Rua, 200",
                    "São Paulo",
                    "SP",
                    "01310100"
            );

            ClienteResponseDTO resposta = service.atualizar(1L, requestAtualizado);

            assertThat(resposta.nome()).isEqualTo("Victor Leal Atualizado");
            verify(repository, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("deve lançar ClienteNotFoundException ao atualizar id inexistente")
        void deveLancarExcecao_quandoIdNaoExisteNaAtualizacao() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.atualizar(99L, requestValido))
                    .isInstanceOf(ClienteNotFoundException.class)
                    .hasMessageContaining("99");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deletar()")
    class Deletar {

        @Test
        @DisplayName("deve deletar cliente quando id existe")
        void deveDeletar_quandoIdExiste() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            // Não lança exceção — se lançar, o teste falha
            assertThatCode(() -> service.deletar(1L))
                    .doesNotThrowAnyException();

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("deve lançar ClienteNotFoundException ao deletar id inexistente")
        void deveLancarExcecao_quandoIdNaoExisteNaDelecao() {
            when(repository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> service.deletar(99L))
                    .isInstanceOf(ClienteNotFoundException.class)
                    .hasMessageContaining("99");

            verify(repository, never()).deleteById(any());
        }
    }
}
