package com.seunome.cadastro_clientes.service;

import com.seunome.cadastro_clientes.dto.ClienteRequestDTO;
import com.seunome.cadastro_clientes.dto.ClienteResponseDTO;
import com.seunome.cadastro_clientes.entity.Cliente;
import com.seunome.cadastro_clientes.exception.ClienteNotFoundException;
import com.seunome.cadastro_clientes.exception.CpfJaCadastradoException;
import com.seunome.cadastro_clientes.exception.EmailJaCadastradoException;
import com.seunome.cadastro_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (repository.existsByCpf(dto.getCpf())) {
            throw new CpfJaCadastradoException(dto.getCpf());
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        Cliente cliente = converterParaEntidade(dto);
        Cliente salvo = repository.save(cliente);
        return converterParaDTO(salvo);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        return converterParaDTO(cliente);
    }

    @Override
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::converterParaDTO);
    }

    @Override
    public Page<ClienteResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(this::converterParaDTO);
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        atualizarDados(cliente, dto);
        Cliente atualizado = repository.save(cliente);
        return converterParaDTO(atualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private Cliente converterParaEntidade(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setDataNascimento(dto.dataNascimento());
        cliente.setEndereco(dto.endereco());
        cliente.setCidade(dto.cidade());
        cliente.setEstado(dto.estado());
        cliente.setCep(dto.cep());
        return cliente;
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDataNascimento(),
                cliente.getEndereco(),
                cliente.getCidade(),
                cliente.getEstado(),
                cliente.getCep(),
                cliente.getDataCadastro()
        );
    }

    private void atualizarDados(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setDataNascimento(dto.dataNascimento());
        cliente.setEndereco(dto.endereco());
        cliente.setCidade(dto.cidade());
        cliente.setEstado(dto.estado());
        cliente.setCep(dto.cep());
    }
}
