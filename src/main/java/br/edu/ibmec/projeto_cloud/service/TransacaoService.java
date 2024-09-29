package br.edu.ibmec.projeto_cloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ibmec.projeto_cloud.dto.TransacaoResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.*;
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
    public TransacaoResponseDTO autorizarTransacao(int cartaoId, Transacao transacao) {
        // Buscar o objeto Cartao pelo ID
        Cartao cartao = cartaoRepository.findById(cartaoId)
            .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        // Regra 1: Verificar se o cartão está ativo
        if (!cartao.getEstaAtivado()) {
            throw new CartaoInativoException("Cartão não está ativo");
        }

        // Regra 2: Verificar se o valor da transação excede o limite disponível
        if (transacao.getValor() > cartao.getSaldo()) {
            throw new LimiteInsuficienteException("Limite insuficiente");
        }

        // Regra 3: Verificar alta frequência de transações (mais de 3 em 2 minutos)
        LocalDateTime doisMinutosAtras = transacao.getDataTransacao().minusMinutes(2);
        List<Transacao> transacoesRecentes = transacaoRepository.findByCartaoAndDataTransacaoAfter(cartao, doisMinutosAtras);

        if (transacoesRecentes.size() >= 3) {
            throw new FrequenciaAltaTransacoesException("Alta frequência de transações em pequeno intervalo");
        }

        // Regra 4: Verificar se há transação duplicada (mesmo valor e comerciante em 2 minutos)
        boolean transacaoDuplicada = transacoesRecentes.stream()
            .anyMatch(t -> t.getValor() == transacao.getValor() && t.getComerciante().equals(transacao.getComerciante()));

        if (transacaoDuplicada) {
            throw new TransacaoDuplicadaException("Transação duplicada");
        }

        // Se todas as regras forem aprovadas, adicionar a transação
        cartao.adicionarTransacao(transacao);

        // Atualizar saldo e persistir dados do cartão
        cartao.setSaldo(cartao.getSaldo() - transacao.getValor());
        cartaoRepository.save(cartao);

        // Persistir os dados da transação
        transacaoRepository.save(transacao);

        return new TransacaoResponseDTO("Sucesso", "Transação Autorizada");
    }

    // Método para buscar todas as transações aprovadas de um cliente
    public List<Transacao> buscarTransacoesPorCliente(int clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));

        // Busca todas as transações associadas aos cartões do cliente
        return cliente.getCartoes().stream()
            .flatMap(cartao -> cartao.getTransacoes().stream())
            .collect(Collectors.toList());
    }
}
