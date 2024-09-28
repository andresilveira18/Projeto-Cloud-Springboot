package br.edu.ibmec.projeto_cloud.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;

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
    public Cliente associarCartao(int clienteId, int cartaoId) {
        // Buscar o cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Buscar o cartão
        Cartao cartao = cartaoRepository.findById(cartaoId)
            .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Associar o cartão ao cliente
        cliente.associarCartao(cartao);
        return clienteRepository.save(cliente);
    }

    // Método para buscar um cliente por ID
    public Optional<Cliente> buscarPorId(int clienteId) {
        return clienteRepository.findById(clienteId);
    }
}
