package com.seunome.cadastro_clientes.service;

import com.seunome.cadastro_clientes.dto.ClienteRequestDTO;
import com.seunome.cadastro_clientes.dto.ClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    ClienteResponseDTO criar(ClienteRequestDTO dto);

    ClienteResponseDTO buscarPorId(Long id);

    Page<ClienteResponseDTO> listarTodos(Pageable pageable);

    Page<ClienteResponseDTO> buscarPorNome(String nome, Pageable pageable);

    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);

    void deletar(Long id);
}
