package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.exception.CartaoNaoEncontradoException;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    public CartaoService(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    // Método para criar um novo cartão
    @Transactional
    public Cartao criarCartao(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    // Método para buscar um cartão por ID
    public Cartao buscarPorId(int cartaoId) {
        return cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));
    }

    public Cliente buscarClientePorCartaoId(int cartaoId) {
        Cartao cartao = buscarPorId(cartaoId);
        return cartao.getCliente(); // Certifique-se de que o Cartao tenha o método getCliente()
    }
   

}
