package br.edu.ibmec.projeto_cloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.repository.TransacaoRepository;

@Service
public class TransacaoService {

    private final CartaoRepository cartaoRepository;
    private final TransacaoRepository transacaoRepository;
    private final ClienteRepository clienteRepository;

    public TransacaoService(CartaoRepository cartaoRepository, TransacaoRepository transacaoRepository, ClienteRepository clienteRepository) {
        this.cartaoRepository = cartaoRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
    }

    // Método para autorizar uma transação com base nas regras de negócio
    @Transactional
    public String autorizarTransacao(int cartaoId, Transacao transacao) {
        // Buscar o objeto Cartao pelo ID
        Cartao cartao = cartaoRepository.findById(cartaoId)
            .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Regra 1: Verificar se o cartão está ativo
        if (!cartao.getEstaAtivado()) {
            return "Cartão não ativo";
        }

        // Regra 2: Verificar se o valor da transação excede o limite disponível
        if (transacao.getValor() > cartao.getSaldo()) {
            return "Limite insuficiente";
        }

        // Regra 3: Verificar alta frequência de transações (mais de 3 em 2 minutos)
        LocalDateTime doisMinutosAtras = transacao.getDataTransacao().minusMinutes(2);
        List<Transacao> transacoesRecentes = transacaoRepository.findByCartaoAndDataTransacaoAfter(cartao, doisMinutosAtras);

        if (transacoesRecentes.size() >= 3) {
            return "Alta frequência de transações em pequeno intervalo";
        }

        // Regra 4: Verificar se há transação duplicada (mesmo valor e comerciante em 2 minutos)
        boolean transacaoDuplicada = transacoesRecentes.stream()
            .anyMatch(t -> t.getValor() == transacao.getValor() && t.getComerciante().equals(transacao.getComerciante()));

        if (transacaoDuplicada) {
            return "Transação duplicada";
        }

        // Se todas as regras forem aprovadas, adicionar a transação
        cartao.adicionarTransacao(transacao);
        transacaoRepository.save(transacao);

        // Atualizar saldo
        cartao.setSaldo(cartao.getSaldo() - transacao.getValor());
        cartaoRepository.save(cartao);

        return "Transação autorizada";
    }

    // Método para buscar todas as transações aprovadas de um cliente
    public List<Transacao> buscarTransacoesPorCliente(int clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Busca todas as transações associadas aos cartões do cliente
        return cliente.getCartoes().stream()
            .flatMap(cartao -> cartao.getTransacoes().stream())
            .collect(Collectors.toList());
    }
}
