package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.dto.CartaoResponseDTO;
import br.edu.ibmec.projeto_cloud.dto.ClienteResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.CartaoNaoEncontradoException;
import br.edu.ibmec.projeto_cloud.exception.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CartaoRepository cartaoRepository;

    public ClienteService(ClienteRepository clienteRepository, CartaoRepository cartaoRepository) {
        this.clienteRepository = clienteRepository;
        this.cartaoRepository = cartaoRepository;
    }

    // Método para criar um novo cliente
    @Transactional
    public Cliente criarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Método para associar um cartão existente a um cliente usando o ID do cartão
    @Transactional
    public ClienteResponseDTO associarCartao(int clienteId, int cartaoId) {
        // Buscar o cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));

        // Buscar o cartão
        Cartao cartao = cartaoRepository.findById(cartaoId)
            .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        // Associar o cartão ao cliente
        cliente.associarCartao(cartao);
        clienteRepository.save(cliente);
        // Mapear os cartões para CartaoResponseDTO
        List<CartaoResponseDTO> cartoesDTO = new ArrayList<>();
        for (Cartao c : cliente.getCartoes()) {
            CartaoResponseDTO cartaoResponseDTO = new CartaoResponseDTO(
                    c.getId(),
                    c.getNumeroCartao(),
                    c.getLimite(),
                    c.getSaldo(),
                    c.getEstaAtivado()
            );
            cartoesDTO.add(cartaoResponseDTO);
        }

        // Retornar o cliente
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getEndereco(), cartoesDTO);
    }

    // Método para buscar um cliente por ID
    public Optional<Cliente> buscarPorId(int clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    
}
